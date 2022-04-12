package main;

import main.model.*;


import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SiteController {


    private static volatile boolean isIndexing = false;

    private List<Thread> threadList = new Vector<>();

    private List<ForkJoinPool> poolList = new Vector<>();

    private DBConnection dbConnection;

    private static Map<String,Double> selectors = new ConcurrentHashMap<>();

    private static LuceneMorphology luceneMorph;

    @Autowired
    private final SiteRepository siteRepository;

    @Autowired
    private final FieldRepository fieldRepository;

    @Autowired
    private final PageRepository pageRepository;

    @Autowired
    private final LemmaRepository lemmaRepository;

    @Autowired
    private final IndexRepository indexRepository;

    @Autowired
    private YAMLConfig myConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static List<Site> siteList = new ArrayList<>();

    public static LuceneMorphology getLuceneMorph() {
        return luceneMorph;
    }

    public static void setLuceneMorph(LuceneMorphology luceneMorph) {
        SiteController.luceneMorph = luceneMorph;
    }

    public static Map<String, Double> getSelectors() {
        return selectors;
    }

    public static void setSelectors(Map<String, Double> selectors) {
        SiteController.selectors = selectors;
    }

    public static List<Site> getSiteList() {
        return siteList;
    }

    public SiteRepository getSiteRepository() {
        return siteRepository;
    }


    public FieldRepository getFieldRepository() {
        return fieldRepository;
    }

    public PageRepository getPageRepository() {
        return pageRepository;
    }

    public LemmaRepository getLemmaRepository() {
        return lemmaRepository;
    }

    public IndexRepository getIndexRepository() {
        return indexRepository;
    }

    public static boolean getIsIndexing() {
        return isIndexing;
    }

    public SiteController(SiteRepository siteRepository,
                          FieldRepository fieldRepository,
                          PageRepository pageRepository,
                          LemmaRepository lemmaRepository,
                          IndexRepository indexRepository) {
        this.siteRepository = siteRepository;
        this.fieldRepository = fieldRepository;
        this.pageRepository = pageRepository;
        this.lemmaRepository = lemmaRepository;
        this.indexRepository = indexRepository;
        try {
            setLuceneMorph(new RussianLuceneMorphology());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/startIndexing")
    public ResponseMessage startIndexing(){
        ResponseMessage responseMessage = new ResponseMessage();
        String userAgent = myConfig.getUserAgent();
        if(isIndexing){
            responseMessage.setResult(false);
            responseMessage.setError("Индексация уже запущена");
        } else {
            responseMessage.setResult(true);
            responseMessage.setError("");
            isIndexing = true;
            AtomicInteger count = new AtomicInteger(0);

            indexation(userAgent, count);
        }
        return responseMessage;
    }

    private void indexation(String userAgent, AtomicInteger count) {
        new Thread( () -> {
            synchronized (pageRepository) {
                pageRepository.deleteAll();
            }
            synchronized (indexRepository) {
                dbConnection.cleanIndex();
            }
            synchronized (lemmaRepository) {
                lemmaRepository.deleteAll();
            }
        }).start();

        threadsRun(userAgent, count);
    }

    private void poolsStop(){
        for (ForkJoinPool pool : poolList) {
            pool.shutdown();
        }
    }

    private void poolsStopNow() {
        for (ForkJoinPool pool : poolList) {
            pool.shutdownNow();
        }
        new Thread(() -> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Получена команда остановки индексации");
        }).start();

    }

    private void threadsRun(String userAgent, AtomicInteger count) {
        for(Site site : siteList) {
            Thread thread = new Thread(() -> {
                    site.setStatus(SiteType.INDEXING);
                    site.setStatusTime(LocalDateTime.now());
                    siteRepository.save(site);
                    ForkJoinPool pool = new ForkJoinPool();
                    poolList.add(pool);
                    List<String> toFileString = pool.invoke(new ReferenceFinder
                            (slashAdding(site.getUrl()),
                                    userAgent, dbConnection,site.getId(),false));
                    site.setStatus(SiteType.INDEXED);
                    site.setStatusTime(LocalDateTime.now());
                    Detailed detailed = new Detailed();
                    dbConnection.getLastError(site,detailed);
                    site.setLastError(detailed.getCode() + " - " + detailed.getError());
                    if(detailed.getCode() >= 400) {
                        site.setStatus(SiteType.FAILED);
                    }
                    siteRepository.save(site);
                    count.getAndIncrement();
                    System.out.println(count.get());
                    areAllIndexed(count);
                    System.out.println("Индексация завершена для сайта " + site.getUrl() );
            });
            threadList.add(thread);
            thread.start();
        }
        poolsStop();

    }

    private void areAllIndexed(AtomicInteger count) {
        if (count.get() == siteList.size()) {
             isIndexing = false;
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseMessage stopIndexing(){

        ResponseMessage responseMessage = new ResponseMessage();
        if(isIndexing){
            responseMessage.setResult(true);
            responseMessage.setError("");
            isIndexing = false;
            poolsStopNow();
        } else {
            responseMessage.setResult(false);
            responseMessage.setError("Индексация не запущена");
        }
        return responseMessage;
    }

    @PostMapping("/indexPage")
    public ResponseMessage addToIndex(@RequestParam(name = "url") String url){
        ResponseMessage responseMessage = new ResponseMessage();

        String userAgent = myConfig.getUserAgent();

        Site site = checkUrl(url, false);

        boolean res = site != null;


        if(isIndexing){
            responseMessage.setError("Индексация уже запущена!");
            responseMessage.setResult(res);
            return responseMessage;
        } else if (res) {
            new Thread (() -> {
                synchronized (indexRepository) {
                    isIndexing = true;
                    System.out.println("Блокировка индексации включена");
                    deleteOldEntries(url, site);
                    ReferenceFinder referenceFinder = new ReferenceFinder(url,userAgent,dbConnection,site.getId(),true);
                    referenceFinder.compute();
                    isIndexing = false;
                    System.out.println("Блокировка индексации выключена");
                }
            } ).start();
        }

        responseMessage.setResult(res);
        if(!res){
           responseMessage.setError("Данная страница находится за пределами " +
                   "сайтов, " +
                   "указанных в конфигурационном файле");
        }

        return responseMessage;
    }

    private void deleteOldEntries(String url, Site site) {
        try {
            int pageId = dbConnection.getIdByPath(ReferenceFinder.getPathName(url),site.getId());
            dbConnection.deleteEntriesForPage(pageId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Site checkUrl(String url, boolean res) {

        Site siteElement = null;

        String fullUrl = "http://" + url.trim();
        String fullUrlHttps = "https://" + url.trim();
        if (url.length() >= 7 && url.trim().substring(0, 7).equals("http://")) {
            fullUrl = url.trim();
        }
        if (url.length() >= 8 && url.trim().substring(0,8).equals("https://")) {
            fullUrlHttps = url.trim();
        }

        for(Site site : siteList){
            if(fullUrl.length() >= site.getUrl().length() &&
                    fullUrl.substring(0,site.getUrl().length()).equals(site.getUrl())) {
                siteElement = site;
            }
            if(fullUrlHttps.length() >= site.getUrl().length() &&
                    fullUrlHttps.substring(0,site.getUrl().length()).equals(site.getUrl())) {
                siteElement = site;
            }
        }
        return siteElement;
    }

    @GetMapping("/statistics")
    public ResponseStatistics getStatistics(){
        init();
        ResponseStatistics responseStatistics = new ResponseStatistics();
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        Total total = new Total();
        total.setSites(getSiteList().size());
        total.setIndexing(false);
        List<Detailed> detailedList = dbConnection.getDetaileds(total);

        statisticsInfo.setTotal(total);
        statisticsInfo.setDetailed(detailedList);
            responseStatistics.setResult(true);
            responseStatistics.setStatistics(statisticsInfo);

        return responseStatistics;
    }

    private void deleteOldSites(List<YAMLConfig.SiteRead> sites) {
        Iterable<Site> siteIterable = siteRepository.findAll();
        List<Site> sitesToBeDelete = new ArrayList<>();
        for(Site site1 : siteIterable){
            boolean exists = false;
            for(YAMLConfig.SiteRead site : sites){
                if(site1.getUrl().equals(site.getUrl())){
                    exists = true;
                }
            }
            if(!exists){
                sitesToBeDelete.add(site1);
            }
        }
        siteRepository.deleteAll(sitesToBeDelete);
    }

    private void addOrUpdateInfo(List<YAMLConfig.SiteRead> sites) {
        for(YAMLConfig.SiteRead site : sites) {
            Iterable<Site> siteIterable = siteRepository.findAll();
            boolean exists = false;
            for(Site site1 : siteIterable){
                exists = isExists(site, exists, site1);
            }

            if(!exists) {
                Site siteModel = new Site();
                siteModel.setName(site.getName());
                siteModel.setUrl(site.getUrl());
                siteModel.setStatus(SiteType.NONE);
                siteModel.setLastError("");
                siteModel.setStatusTime(LocalDateTime.now());
                siteRepository.save(siteModel);
                siteList.add(siteModel);
            }
        }
    }

    private boolean isExists(YAMLConfig.SiteRead site, boolean exists, Site site1) {
        if(site1.getUrl().equals(site.getUrl())){
            site1.setName(site.getName());
            siteRepository.save(site1);
            siteList.add(site1);
            exists = true;
        }
        return exists;
    }

    private void init(){
        List<YAMLConfig.SiteRead> sites;
        sites = myConfig.getSites();

        try {
            dbConnection = new DBConnection(this, jdbcTemplate);
            setSelectors( dbConnection.getSelectors());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        siteList.clear();
        addOrUpdateInfo(sites);
        deleteOldSites(sites);
    }

    private static String slashAdding(String url) {
        if (url.charAt(url.length() - 1) == '/') {
            return url;
        } else {
            return url + "/";
        }
    }

}
