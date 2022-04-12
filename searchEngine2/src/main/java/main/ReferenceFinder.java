package main;

import main.model.Index;
import main.model.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ReferenceFinder extends RecursiveTask<List<String>> {

    private final String node;

    private final String userAgent;

    private final DBConnection dbConnection;

    private boolean onlyIndexation;

    private final int siteId;


    public ReferenceFinder(String node, String userAgent, DBConnection dbConnection, int siteId,
                           boolean onlyIndexation) {
        this.node = node;
        this.userAgent = userAgent;
        this.dbConnection = dbConnection;
        this.siteId = siteId;
        this.onlyIndexation = onlyIndexation;
    }


    public List<String> getChildren(Document document) {
        List<String> children = null;
        Set<String> childSet = new HashSet<>();

        try {
            Thread.sleep(1000);
            Elements elements = document.select("a[href],link[href]");
            for (Element element : elements) {
                setFormation(node, childSet, element);
            }
            children = childSet.stream().collect(Collectors.toList());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return children;
    }

    private void setFormation(String node, Set<String> childSet, Element element) {
        if (element.attr("abs:href").length() > node.length() &&
                element.attr("abs:href").substring(0, node.length()).compareTo(node) == 0) {
            String substr = element.attr("abs:href").substring(node.length());
            childSet.add(substr);
        }
    }

    @Override
    protected List<String> compute() {

        List<String> resultSet = new ArrayList<>();
        if(node.contains("#")) {
            return resultSet;
        }
        Document document = null;
        List<String> children = null;

        Page page = new Page();
        page.setPath(getPathName(node));

        try {
            document = getStatusCode(page);
            formAndExecuteInsertQuery(page);
            if(document != null) {
                List<Block> blocks = getBlocks(document);
                formDB(blocks);
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        if(onlyIndexation){
            return resultSet;
        }
        if (node.lastIndexOf("/") > node.lastIndexOf(".")
                && document != null
        ) {
            children = getChildren(document);
        }

        if (children != null && !children.isEmpty()) {
            List<ReferenceFinder> taskList = new ArrayList<>();
            taskListFormation(children, taskList);
            resultFormation(resultSet, taskList);
        } else {
            resultSet.add(node);
        }
        return resultSet;
    }


    private void formDB(List<Block> blocks) throws SQLException {
        Map<Index,Double> indexMap = new HashMap<>();
        Lemmatizer lemmatizer = new Lemmatizer(SiteController.getLuceneMorph());
        for(Block block : blocks) {
            Map<String,Integer> lemmaMap = new HashMap<>();
            lemmatizer.analyzer(block.getBlockString(),lemmaMap);
            dbConnection.insertLemmas(lemmaMap,siteId);
            formIndex(indexMap,lemmaMap,block.getBlockWeight());
        }

        dbConnection.insertIndex(indexMap,siteId);
    }

    private void formIndex(Map<Index,Double> indexMap, Map<String, Integer> lemmaMap, double blockWeight) throws SQLException {
        for(Map.Entry<String,Integer> entry: lemmaMap.entrySet()){
            Index index = new Index();
            index.setLemmaId(dbConnection.getIdByLemma(entry.getKey(),siteId));
            index.setPageId(dbConnection.getIdByPath(getPathName(node),siteId));
            if(indexMap.containsKey(index)){
                double oldRank = indexMap.get(index);
                indexMap.put(index,oldRank + entry.getValue() * blockWeight);
            } else {
                indexMap.put(index, entry.getValue() * blockWeight);
            }
        }
    }

    private List<Block> getBlocks(Document document) {//Получаем блоки и очищаем их от html-тегов
        List<Block> blocks = new ArrayList<>();
        for(Map.Entry<String,Double> entry : SiteController.getSelectors().entrySet()) {
            Elements elements = document.select(entry.getKey());
            Block block = new Block();
            block.setBlockString(elements.text());
            block.setBlockWeight(entry.getValue());
            blocks.add(block);
        }
        return blocks;
    }

    private Document getStatusCode(Page page) {
        int code = 0;
        Document document = null;
        try {
            Connection.Response response = Jsoup.connect(node)
                    .userAgent(userAgent)
                    .referrer("http://www.google.com").execute();
            code = response.statusCode();
            document = response.parse();
            page.setCode(code);
            getHTML(document, page);
        } catch (IOException e) {
            String message = e.getMessage();
            page.setHtmlCode(e.getMessage());
            String[] words = message.split("=");
            if (words.length > 1) {
                System.out.println(words[1].substring(0, 3));
                code = Integer.parseInt(words[1].substring(0, 3));
                page.setCode(code);
            } else if (message.contains("Unhandled content type. Must be")){
                code = 200;
                page.setCode(code);
            }
        }
        return document;
    }

    private static String mysqlRealEscapeString(String str) {
        if (str == null) {
            return null;
        }

        if (str.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "").length() < 1) {
            return str;
        }

        String strClean;
        strClean = str.replaceAll("\\\\", "\\\\\\\\");
        strClean = strClean.replaceAll("\\n", "\\\\n");
        strClean = strClean.replaceAll("\\r", "\\\\r");
        strClean = strClean.replaceAll("\\t", "\\\\t");
        strClean = strClean.replaceAll("\\00", "\\\\0");
        strClean = strClean.replaceAll("'", "\\\\'");
        strClean = strClean.replaceAll("\\\"", "\\\\\"");

        return strClean;
    }


    private void getHTML(Document document, Page page)  {

        String html = "";
        if (document != null) {
            html = document.toString();
        }

        html = mysqlRealEscapeString(html);

        page.setHtmlCode(html);
    }

    private void formAndExecuteInsertQuery(Page page) throws SQLException {
        dbConnection.formInsertQuery(page.getPath(), page.getCode(), page.getHtmlCode(),siteId);
    }

    public static String getPathName(String node) {
        String path = "";
        int index = node.indexOf('/', 8);
        if (!node.contains("#")) {
            path = node.substring(index);
        }
        return path;
    }

    private void resultFormation(List<String> resultSet, List<ReferenceFinder> taskList)  {
        List<String> resultList = new ArrayList<>();
        resultList.add(node);

        for (ReferenceFinder task : taskList) {
            resultList.addAll(task.join());
            for (String e : resultList) {
                if (e.compareTo(node) != 0) {
                    e = "\t" + e;
                }
                if (!resultSet.contains(e) && !e.contains("#")) {
                    resultSet.add(e);
                }

            }
        }
    }

    private void taskListFormation(List<String> children, List<ReferenceFinder> taskList) {

        for (String child : children) {
            if (child.length() >= 1) {
                String fullChild;
                if (!child.contains(".")) {
                    fullChild = node + child + "/";
                } else {
                    fullChild = node + child;
                }

                ReferenceFinder referenceFinder = new ReferenceFinder(fullChild,userAgent,dbConnection,siteId,
                        false);
                referenceFinder.fork();
                taskList.add(referenceFinder);
            }
        }
    }
}


