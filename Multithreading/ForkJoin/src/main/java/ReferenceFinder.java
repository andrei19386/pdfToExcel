import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class ReferenceFinder extends RecursiveTask<List<String>> {

    private final String node;

    public ReferenceFinder(String node) {
        this.node = node;
    }

    public List<String> getChildren(String node) {
        List<String> children = null;
        Set<String> childSet = new HashSet<>();

        try {
            Document document = Jsoup.connect(node).get();
            Thread.sleep(200);
            Elements elements = document.select("a[href],link[href]");
            for (Element element : elements) {
                setFormation(node, childSet, element);
            }
            children = childSet.stream().toList();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return children;
    }

    private void setFormation(String node, Set<String> childSet, Element element) {
        if (element.attr("abs:href").length() > node.length() &&
                element.attr("abs:href").substring(0, node.length()).compareTo(node) == 0) {
            String substr = element.attr("abs:href").substring(node.length());
            int index = substr.indexOf("/");
            if (index > 0) {
                childSet.add(substr.substring(0, index));
            } else {
                childSet.add(substr);
            }
        }
    }

    @Override
    protected List<String> compute() {
        List<String> resultSet = new ArrayList<>();

        if (node.lastIndexOf("/") > node.lastIndexOf(".")) {//Если node по имени не является конечным файлом
            // с расширением - чтобы не тратить время на поиск дочерних ссылок
            List<String> children = getChildren(node);
            if (children != null && !children.isEmpty()) {
                List<ReferenceFinder> taskList = new ArrayList<>();
                taskListFormation(children, taskList);
                resultFormation(resultSet, node, taskList);
            } else {
                resultSet.add(node);
            }
        } else {
            resultSet.add(node);
        }
        return resultSet;
    }

    private void resultFormation(List<String> resultSet, String node, List<ReferenceFinder> taskList) {
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

                ReferenceFinder referenceFinder = new ReferenceFinder(fullChild);
                referenceFinder.fork();
                taskList.add(referenceFinder);
            }
        }
    }
}