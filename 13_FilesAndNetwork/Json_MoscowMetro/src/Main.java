import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.ParseException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static final String URL = "https://www.moscowmap.ru/metro.html#lines";
    public static final String JSON_FILE = "out/map.json";

    public static void main(String[] args) {
        try {
            //Document doc = getJsoupDocument("data/htmlcode.html"); Для отладки

             Document doc = Jsoup.connect(URL).maxBodySize(0).get();
            List<LineData> lines = getLines(doc);
            Set<Station> stationSet = getStationsSet(getStationIndexes(doc));

            parseAndWriteJSONFile(doc, lines, stationSet);
            printInfoFromJson();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void printInfoFromJson(){
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(JSON_FILE));
            lines.forEach(line -> builder.append(line));

            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(builder.toString());

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            Map<String,Integer> countStations = countStationsOnEachLine(stationsObject);
            System.out.println("________________________________________________");
            for (Map.Entry<String, Integer> entry : countStations.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();
                System.out.println("Линия " + key + ", количество станций - " + value);
            }
            System.out.println("________________________________________________");
            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            AtomicInteger countConnections = countConnectionsFunction(connectionsArray);
            System.out.println("Количество переходов: " + countConnections);
            System.out.println("________________________________________________");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Map<String,Integer> countStationsOnEachLine(JSONObject stations) {
        Map<String,Integer> countStations = new HashMap<>();
        stations.keySet().forEach(lineNumberObj ->{
            String lineNumber = lineNumberObj.toString();
            JSONArray stationsArray = (JSONArray) stations.get(lineNumberObj);
            countStations.put(lineNumber, stationsArray.size());
        });
        return countStations;
    }

    private static AtomicInteger countConnectionsFunction(JSONArray connectionsArray) {
        AtomicInteger count = new AtomicInteger();
        connectionsArray.forEach(connectionObject -> count.getAndIncrement());
        return count;
    }

    private static void parseAndWriteJSONFile(Document doc, List<LineData> lines, Set<Station> stationSet)
            throws IOException, ParseException {
        JSONObject lineJSON = new JSONObject();
        JSONArray linesJSONArray = new JSONArray();
        fullJSONLines(doc, lines, stationSet, lineJSON, linesJSONArray);

        bugCorrection(stationSet);
        Map<Integer, Station> stationMap = generateStationMap(stationSet);

        JSONObject objJSONObject = new JSONObject();
        Set<String> connectionsSet = getConnectionsSet(doc); // Получаем список переходов

        Set<String> reducedConnectionSet = setReduce(connectionsSet, stationSet);//Объединяем переходы:
        // Например, Переходы с Чеховской на Пушкинскую и Тверской на Пушкинскую можно объединить
        // в переход из 3 станций

        JSONArray connections = new JSONArray();
        fullJSONStationsOnConnection(stationMap, reducedConnectionSet, connections);

        objJSONObject.put("stations",lineJSON);
        objJSONObject.put("connections",connections);
        objJSONObject.put("lines",linesJSONArray);

        writeFormattedFile(objJSONObject);
    }

    private static void fullJSONLines(Document doc, List<LineData> lines, Set<Station> stationSet, JSONObject lineJSON, JSONArray linesJSONArray) throws IOException {
        for(LineData line : lines) {
            JSONObject lineJSONObject = new JSONObject();
            lineJSONObject.put("number",line.getDataNumber());
            lineJSONObject.put("name",line.getName());
            linesJSONArray.add(lineJSONObject);

            List<String> stations = getStations(doc,line.getDataNumber());// Получаем список станций
            // по идентификатору линии
            stations.forEach(s -> {
                setLineIdOfStation(s,line.getDataNumber(), stationSet);
            });
            JSONArray stationsJSON = new JSONArray();
            stationsJSON.addAll(stations);
            lineJSON.put(line.getDataNumber(),stationsJSON);
        }
    }

    private static void fullJSONStationsOnConnection(Map<Integer, Station> stationMap,
                                                     Set<String> reducedConnectionSet, JSONArray connections) {
        for(String connection : reducedConnectionSet){
            String[] words = connection.split("[ ]");
            JSONArray stationsOnConnection = new JSONArray();
            for(String word : words) {
                int key = Integer.parseInt(word);
                Station stationByKey = getStationByIndex(key, stationMap);
                JSONObject stationOfConnection = new JSONObject();
                stationOfConnection.put("line",stationByKey.getLineId());
                stationOfConnection.put("station",stationByKey.getName());
                stationsOnConnection.add(stationOfConnection);
            }
            connections.add(stationsOnConnection);
        }
    }

    private static Map<Integer,Station> generateStationMap(Set<Station> stationSet) {
        Map<Integer,Station> stationMap = new HashMap<>();

        stationSet.forEach(s ->  stationMap.put(s.getStationIndex(),s)   );

        return stationMap;
    }

    private static void bugCorrection(Set<Station> stationSet) {
        Station stationMissed = new Station("Царицыно");//Баг станции Царицыно
        // - в списке индексов станций она не найдена, поскольку отсутствует в списках по атрибуту text
        stationMissed.setLineId("D2");
        stationMissed.setStationIndex(315);
        stationSet.add(stationMissed);
    }

    private static void writeFormattedFile(JSONObject objJSONObject) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(objJSONObject.toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter file = new FileWriter(JSON_FILE);
        file.write(gson.toJson(json));
        file.flush();
        file.close();
    }

    private static Document getJsoupDocument(String path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> strings = Files.readAllLines(Paths.get(path), Charset.forName("windows-1251"));
        strings.forEach(string -> stringBuilder.append(string + "\n"));
        return Jsoup.parse(stringBuilder.toString());
    }

    private static List<LineData> getLines(Document doc) throws IOException {
        List<LineData> elements = new ArrayList<>();
        Elements tagElements = doc.select("span[class^=js-metro-line]");
        int index = 0;
        for (Element tagElement : tagElements) {
            String dataLine = tagElement.attr("data-line");
            LineData lineData = new LineData(dataLine,tagElement.text());
            elements.add(lineData);
            index++;
        }
        return elements;
    }

    private static List<String> getStations(Document doc,String line) throws IOException  {
        List<String> elements = new ArrayList<>();

        String dataLine = "";
        Elements tagElements = doc.select("span[class^=js-metro-line], span[class^=name]");
        boolean isThisLine = false;
        for (Element tagElement : tagElements) {
            dataLine = tagElement.attr("data-line");
            if(dataLine.compareTo(line) == 0) {//Непустое значение dataLine соответствует названию линии
                isThisLine = true;
                continue;
            } else if (dataLine != "") {//Пустое значение dataLine соответствует названию станции
                isThisLine = false;//Как только нашли новое название линии, переключаем флаг принадлежности
                continue;
            }
            if(isThisLine){
                elements.add(tagElement.text());
            }

        }
        return elements;
    }

    private static Map<String,String> getStationIndexes(Document doc){//Генерируем Map из идентификатора/ов
        // станции и ее названия (для случая сложных названий, на нескольких строках)
        Map<String,String> stationIndexes = new HashMap<>();
        Elements elements = doc.select("text");
        for(Element element : elements) {
            String attribute = element.attr("class");
            String text = element.text();
            if(text.lastIndexOf("-")==text.length()-1){
                text = text.substring(0,text.length()-1);
            } //Баг станции Москва Товарная с символом дефиса, которого не должно быть
            if(!stationIndexes.containsKey(attribute)) {
                stationIndexes.put(attribute,text);
            } else {
                if(stationIndexes.get(attribute).length() != 1) {
                    stationIndexes.put(attribute, stationIndexes.get(attribute) + " " + text);
                } else {
                    stationIndexes.put(attribute, stationIndexes.get(attribute) + text); //Баг станции Ольховая
                }
            }
        }
        return stationIndexes;
    }

    private static Set<Station> getStationsSet(Map<String,String> stationIndexes) {//Генерируем список станций
        // со всеми идентификаторами станций, это нужно для генерации переходов
        Set<Station> stationSet = new HashSet<>();
        for (Map.Entry<String, String> entry : stationIndexes.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            String[] words = key.split("[ ]");
            for(int i = 0; i < words.length; i++){
                Station station = new Station(value);
                if(words[i].substring(1).matches("[0-9]+")) {
                    station.setStationIndex(Integer.parseInt(words[i].substring(1)));
                    stationSet.add(station);
                }
            }
        }
        return stationSet;
    }

    private static void setLineIdOfStation(String nameOfStation, String indexOfLine, Set<Station> stationSet) {
        String treatmentString;
        String treatmentNameOfStation = nameOfStation.replace("ё", "е");//Различие написаний
        // станций с буквой ё

        if(nameOfStation.compareTo("Библиотека имени Ленина") == 0) {//Баг станции Библиотека имени Ленина
            treatmentNameOfStation = "Библиотека им. Ленина";
        }

        for (int i = 0; i < stationSet.size(); i++) {
            treatmentString = stationSet.stream().toList().get(i).getName().replace("ё", "е");

            if (treatmentString.toLowerCase().trim().compareTo(treatmentNameOfStation.toLowerCase().trim()) == 0 &&
                    stationSet.stream().toList().get(i).getLineId() == null) {
                stationSet.stream().toList().get(i).setLineId(indexOfLine);
                break;
            }
        }

    }

    private static Set<String> getConnectionsSet(Document doc) {
        Set<String> connectionsList = new HashSet<>();
        getConnectionsByCss(doc,connectionsList,"line[class^=p]");
        getConnectionsByCss(doc,connectionsList,"path[class^=p]");

        return  connectionsList;
    }

    private static Station getStationByIndex(int stationIndex, Map<Integer,Station> stationMap){

        return stationMap.get(stationIndex);
    }

    private static Set<String> getConnectionsByCss(Document doc, Set<String> connectionsList,String css) {
        Elements elements = doc.select(css);
        for(Element element : elements ) {
            String connection = element.attr("class");
            String keyFirst = connection.split("[ ]")[0].substring(1);
            String keySecond = connection.split("[ ]")[1].substring(1);
            if(keyFirst.matches("[0-9]+") && keySecond.matches("[0-9]+") ) {
                String connectionIndexes;
                if(Integer.parseInt(keyFirst) < Integer.parseInt(keySecond)) {
                    connectionIndexes = keyFirst + " " + keySecond;
                } else {
                    connectionIndexes = keySecond + " " + keyFirst;
                }
                connectionsList.add(connectionIndexes);
            }
        }
        return connectionsList;
    }

    private static Set<String> setReduce(Set<String> connectionSet, Set<Station> stationSet) {
        Set<String> reducedSet = new HashSet<>();
        List<Connection> connectionList = new ArrayList<>();
        connectionSet.forEach(s -> {
            String[] words = s.split("[ ]");
            int first = Integer.parseInt(words[0]);
            int second = Integer.parseInt(words[1]);
            connectionList.add(new Connection(first,second));
            connectionList.add(new Connection(second,first));
        });


        fullReducedSet(stationSet, reducedSet, connectionList);
        return  reducedSet;
    }

    private static void fullReducedSet(Set<Station> stationSet, Set<String> reducedSet,
                                       List<Connection> connectionList) {
        Set<Integer> connections = new HashSet<>();
        for(Station station : stationSet) {
            String s = "";
            if(!connections.contains(station.getStationIndex())) {
                Set<Integer> temporarySet = findAllConnections(station.getStationIndex(), connectionList);
                connections.addAll(temporarySet);
                s = getString(s, temporarySet);
            }
            if(s != "") {
                reducedSet.add(s);
            }
        }
    }

    private static String getString(String s, Set<Integer> temporarySet) {
        boolean isFirst = true;
        for(int i : temporarySet) {
            if(isFirst){
                s = Integer.toString(i);
                isFirst = false;
            } else {
                s = s + " " + i;
            }
        }
        return s;
    }

    private static Set<Integer> findAllConnections(int stationIndex, List<Connection> connectionList){
        List<Connection> foundConnections = new ArrayList<>();
        connectionList.forEach(c -> {
            if(c.getFirst() == stationIndex){
                foundConnections.add(c);
            }
        });

        Set<Integer> foundRecursiveConnections = new HashSet<>();
        List<Connection> newConnectionsList = new ArrayList<>(connectionList);
        foundConnections.forEach(c -> {
            newConnectionsList.remove(c);
            foundRecursiveConnections.add(c.getFirst());
            foundRecursiveConnections.add(c.getSecond());
            if(!newConnectionsList.isEmpty()) {
                foundRecursiveConnections.addAll(findAllConnections(c.getSecond(), newConnectionsList) ) ;
            }
        });
        return foundRecursiveConnections;
    }

}
