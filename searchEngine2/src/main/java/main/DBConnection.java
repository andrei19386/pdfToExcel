package main;

import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.*;


public class DBConnection {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private YAMLConfig myConfig;

    @Autowired
    private  SiteRepository siteRepository;

    @Autowired
    private  FieldRepository fieldRepository;

    @Autowired
    private  PageRepository pageRepository;

    @Autowired
    private  LemmaRepository lemmaRepository;

    @Autowired
    private  IndexRepository indexRepository;

    public DBConnection(SiteController siteController, JdbcTemplate jdbcTemplate) {
        this.siteRepository = siteController.getSiteRepository();
        this.fieldRepository = siteController.getFieldRepository();
        this.pageRepository = siteController.getPageRepository();
        this.lemmaRepository = siteController.getLemmaRepository();
        this.indexRepository = siteController.getIndexRepository();
        this.jdbcTemplate = jdbcTemplate;
    }


    public void executeInsert(StringBuilder insertQuery) throws SQLException {
        String sql = "INSERT INTO page(path,code,content,site_id) VALUES "
                + insertQuery.toString() +
                " ON DUPLICATE KEY UPDATE path = path";

        synchronized (pageRepository) {
            jdbcTemplate.execute(sql);
            System.out.println("Добавляется очередная страница в Базу данных");
        }
    }

    public  Map<String,Double> getSelectors() throws SQLException {
        Map<String,Double> result = new HashMap<>();
       Iterable<Field> fieldIterable = fieldRepository.findAll();
       for(Field field : fieldIterable) {
           result.put(field.getSelector(),field.getWeight());
       }
        return result;
    }

    public int getIdByPath(String path, int siteId) throws SQLException {
        String sql = "SELECT id from page WHERE path='" + path + "' and site_id = '" + siteId + "'";
        return getIntegerSQL(sql);
    }

    public int getIdByLemma(String lemmaString, int siteId) throws SQLException {
        String sql = "SELECT id from lemma WHERE lemma='" + lemmaString + "' and site_id = '" + siteId + "'";
        return getIntegerSQL(sql);
    }

    public int getFrequencyByLemma(String lemmaString, int siteId) throws SQLException {
        String sql = "SELECT frequency from lemma WHERE lemma='" + lemmaString + "' and site_id = '" + siteId + "'";
        return getIntegerSQL(sql);
    }



    public  void formInsertQuery(String path, int code, String content, int siteId) throws SQLException {

        StringBuilder insertQuery = new StringBuilder();

        if (path.equals("")) {
            return;
        }
        insertQuery.append("('" + path + "', '" + code +  "', '" + content + "', " + siteId + " )" );

       executeInsert(insertQuery);

    }


    public int getMaxFrequency() throws SQLException {
        String sql = "SELECT MAX(frequency) AS max_frequency from lemma";
        return getIntegerSQL(sql);
    }

    private int getIntegerSQL(String sql) {
        List<Integer> list = jdbcTemplate.queryForList(sql, Integer.class);
        if( list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return 0;
    }

    public Set<Integer> getPagesId(int lemmaId) throws SQLException {
        Set<Integer> result = new HashSet<>();
        String sql = "SELECT page_id from search_engine.index WHERE lemma_id = " + lemmaId;
        List<Integer> list = jdbcTemplate.queryForList(sql,Integer.class);
        result.addAll(list);
        return result;
    }


    public double getRank(int index, int id) throws SQLException {
        if(index == 0 || id == 0) {
            return 0;
        }
        String sql = "SELECT `rank` from search_engine.index WHERE lemma_id =" + id + " AND page_id = " + index;
        List<Double> idList = jdbcTemplate.queryForList(sql, Double.class);
        if( idList != null && !idList.isEmpty()) {
            return idList.get(0);
        }
        return 0;
    }

    public void insertLemmas(Map<String, Integer> lemmaMap, int siteId) throws SQLException {
        if(lemmaMap.isEmpty()){
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO lemma(lemma,frequency,site_id) VALUES ");
        boolean isFirst = true;
        for(String entry : lemmaMap.keySet()) {
            if(isFirst) {
                sql.append("('" + entry + "', 1," + siteId + ")");
                isFirst = false;
            } else {
                sql.append(", ('" + entry + "', 1," + siteId + ")");
            }
        }
        sql.append(" ON DUPLICATE KEY UPDATE `frequency` = `frequency` + 1 ");
        synchronized (lemmaRepository) {
            jdbcTemplate.execute(sql.toString());
        }
    }

    public  void insertIndex(Map<Index, Double> indexMap, int siteId) throws SQLException {
        if(indexMap.isEmpty()){
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO search_engine.index(page_id, lemma_id, `rank`) VALUES ");
        boolean isFirst = true;
        for(Map.Entry<Index, Double> entry: indexMap.entrySet()) {
            int nodeId = entry.getKey().getPageId();
            int lemmaId = entry.getKey().getLemmaId();
            double rank = entry.getValue();
            if(isFirst) {
                sql.append("(" + nodeId + ", " + lemmaId + ", " + rank + ")");
                isFirst = false;
            } else {
                sql.append(", (" + nodeId + ", " + lemmaId + ", " + rank + ")");
            }

        }
        synchronized (indexRepository) {
            jdbcTemplate.execute(sql.toString());
            System.out.println("Происходит индексация ...");
        }
    }

    public  String findPathById(int pageId) throws SQLException {
        String sql = "SELECT path from search_engine.page WHERE id =" + pageId;

        return getStringSQL(sql);
    }

    public String findContentById(int pageId) throws SQLException {
        String sql = "SELECT content from search_engine.page WHERE id =" + pageId;
        return getStringSQL(sql);
    }

    private String getStringSQL(String sql) {
        List<String> list = jdbcTemplate.queryForList(sql, String.class);
        if( list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return "";
    }


    public Page findPageInfoById(int pageId) throws SQLException {

        Optional<Page> pageOptional = pageRepository.findById(pageId);
       if (pageOptional.isPresent()) {
           return pageOptional.get();
       }
        return null;
    }


    public List<Detailed> getDetaileds(Total total) {
        String sql = "SELECT COUNT(*) FROM search_engine.page";
        List<Integer> countPages = jdbcTemplate.queryForList(sql,Integer.class);
        total.setPages(countPages.get(0));

        sql = "SELECT COUNT(*) FROM search_engine.lemma";
        List<Integer> countLemmas = jdbcTemplate.queryForList(sql,Integer.class);
        total.setLemmas(countLemmas.get(0));

        List<Detailed> detailedList = new ArrayList<>();

        for(Site site : SiteController.getSiteList()){
            Detailed detailedElement = new Detailed();
            detailedElement.setUrl(site.getUrl());
            detailedElement.setName(site.getName());
            detailedElement.setStatus(site.getStatus());
            detailedElement.setStatusTime(site.getStatusTime().toEpochSecond(ZoneOffset.ofHours(3))*1000);
            getLastError(site, detailedElement);
            sql = "SELECT COUNT(*) FROM search_engine.page where site_id = " + site.getId();
            countPages = jdbcTemplate.queryForList(sql,Integer.class);
            sql = "SELECT COUNT(*) FROM search_engine.lemma where site_id = " + site.getId();
            countLemmas = jdbcTemplate.queryForList(sql,Integer.class);
            detailedElement.setPages(countPages.get(0));
            detailedElement.setLemmas(countLemmas.get(0));
            detailedList.add(detailedElement);
        }
        return detailedList;
    }

    public void getLastError(Site site, Detailed detailedElement) {
        String sql;
        sql = "SELECT code, content FROM search_engine.page where site_id = " + site.getId() + " and " +
                " code >= 400 ORDER BY id DESC LIMIT 1";

        Page error = jdbcTemplate.query(sql, new ResultSetExtractor<Page>() {

            @Override
            public Page extractData(ResultSet rs) throws SQLException, DataAccessException {
                Page page = new Page();
                if(rs.next()){
                    page.setCode(rs.getInt("code"));
                    page.setHtmlCode(rs.getString("content"));
                    rs.close();
                    return page;
                }
                rs.close();
                return null;
            }
        });
        if(error != null) {
            detailedElement.setError(error.getHtmlCode());
            detailedElement.setCode(error.getCode());
        } else {
            detailedElement.setCode(200);
            detailedElement.setError("");
        }
    }


    public void cleanIndex(){
        String sql = "DELETE FROM search_engine.index";
        jdbcTemplate.execute(sql);
    }

    public void deleteEntriesForPage (int pageId) throws SQLException {
        String sql = "DELETE FROM search_engine.index WHERE page_id = " + pageId;
        jdbcTemplate.execute(sql);
        sql = "DELETE FROM search_engine.page WHERE id = " + pageId;
        jdbcTemplate.execute(sql);
    }

}