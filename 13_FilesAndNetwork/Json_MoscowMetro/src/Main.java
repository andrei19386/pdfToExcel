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
            //Document doc = getJsoupDocument("data/htmlcode.html");

            Document doc = Jsoup.connect(URL).maxBodySize(0).get();
            List<LineData> lines = getLines(doc);
            List<Station> stationList = getStationsList(getStationIndexes(doc));

            parseAndWriteJSONFile(doc, lines, stationList);
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

    private static void parseAndWriteJSONFile(Document doc, List<LineData> lines, List<Station> stationList)
            throws IOException, ParseException {
        JSONObject lineJSON = new JSONObject();
        JSONArray linesJSONArray = new JSONArray();
        for(LineData line : lines) {
            JSONObject lineJSONObject = new JSONObject();
            lineJSONObject.put("number",line.getDataNumber());
            lineJSONObject.put("name",line.getName());
            linesJSONArray.add(lineJSONObject);

            List<String> stations = getStations(doc,line.getDataNumber());// Получаем список станций
            // по идентификатору линии
            stations.forEach(s -> {
                setLineIdOfStation(s,line.getDataNumber(), stationList);
            });
            JSONArray stationsJSON = new JSONArray();
            stationsJSON.addAll(stations);
            lineJSON.put(line.getDataNumber(),stationsJSON);
        }

        bugCorrection(stationList);

        JSONObject objJSONObject = new JSONObject();
        Set<String> connectionsSet = getConnectionsSet(doc); // Получаем список переходов
        Set<String> reducedConnectionSet = setReduce(connectionsSet, stationList.size());//Объединяем переходы:
        // Например, Переходы с Чеховской на Пушкинскую и Тверской на Пушкинскую можно объединить
        // в переход из 3 станций

        JSONArray connections = new JSONArray();
        for(String connection : reducedConnectionSet){
            String[] words = connection.split("[ ]");
            JSONArray stationsOnConnection = new JSONArray();
            for(String word : words) {
                int key = Integer.parseInt(word);
                Station stationByKey = getStationByIndex(key, stationList);
                JSONObject stationOfConnection = new JSONObject();
                stationOfConnection.put("line",stationByKey.getLineId());
                stationOfConnection.put("station",stationByKey.getName());
                stationsOnConnection.add(stationOfConnection);
            }
            connections.add(stationsOnConnection);
        }

        objJSONObject.put("stations",lineJSON);
        objJSONObject.put("connections",connections);
        objJSONObject.put("lines",linesJSONArray);

        writeFormattedFile(objJSONObject);
    }

    private static void bugCorrection(List<Station> stationList) {
        Station stationMissed = new Station("Царицыно");//Баг станции Царицыно
        // - в списке индексов станций она не найдена, поскольку отсутствует в списках по атрибуту text
        stationMissed.setLineId("D2");
        stationMissed.setStationIndex(315);
        stationList.add(stationMissed);
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
        // станции и ее названия (для случая сложных нахваний, на нескольких строках)
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

    private static List<Station> getStationsList(Map<String,String> stationIndexes) {//Генерируем список станций
        // со всеми идентификаторами станций, это нужно для генерации переходов
        List<Station> stationList = new ArrayList<>();
        for (Map.Entry<String, String> entry : stationIndexes.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            String[] words = key.split("[ ]");
            for(int i = 0; i < words.length; i++){
                Station station = new Station(value);
                if(words[i].substring(1).matches("[0-9]+")) {
                    station.setStationIndex(Integer.parseInt(words[i].substring(1)));
                    stationList.add(station);
                }
            }
        }
        return stationList;
    }

    private static void setLineIdOfStation(String nameOfStation, String indexOfLine, List<Station> stationList) {
        String treatmentString;
        String treatmentNameOfStation = nameOfStation.replace("ё", "е");//Различие написаний
        // станций с буквой ё

        if(nameOfStation.compareTo("Библиотека имени Ленина") == 0) {//Баг станции Библиотека имени Ленина
            treatmentNameOfStation = "Библиотека им. Ленина";
        }

        for (int i = 0; i < stationList.size(); i++) {
            treatmentString = stationList.get(i).getName().replace("ё", "е");


            if (treatmentString.toLowerCase().trim().compareTo(treatmentNameOfStation.toLowerCase().trim()) == 0 &&
                    stationList.get(i).getLineId() == null) {
                stationList.get(i).setLineId(indexOfLine);
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

    private static Station getStationByIndex(int stationIndex, List<Station> stationList){
        for(Station station : stationList) {
            if(station.getStationIndex() == stationIndex) {
                return station;
            }
        }
        return new Station("Invalid Station Name!");
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

    private static Set<String> setReduce(Set<String> connectionList, int sizeOfStationList) {
        Set<String> reducedSet = new HashSet<>();
        boolean[][] incidenceMatrix = new boolean[sizeOfStationList + 3][connectionList.size()];
        List<String> listConnections = connectionList.stream().toList();
        getIncidenceMatrix(incidenceMatrix, listConnections);

        getRedusedSet(sizeOfStationList, reducedSet, incidenceMatrix, listConnections);
        return reducedSet;
    }

    private static void getRedusedSet(int sizeOfStationList, Set<String> reducedSet, boolean[][] incidenceMatrix,
                                      List<String> listConnections) {
        for(int j = 0; j < listConnections.size(); j++) {
            String element = "";
            boolean isFirst = true;
            for(int i = 0; i < sizeOfStationList + 3; i++) {
                if(incidenceMatrix[i][j]){
                    if(isFirst) {
                        element = Integer.toString(i+1);
                        isFirst = false;
                    } else {
                        element = element + " " + Integer.toString(i + 1);
                    }
                }
            }
            if(element != "") {
                reducedSet.add(element);
            }
        }
    }

    private static void getIncidenceMatrix(boolean[][] incidenceMatrix, List<String> listConnections) {
        int keyFirst;
        int keySecond;

        Set<Integer> keySet = new HashSet<>();// Содержит все обработанные идентификаторы станций
        // за весь период выполнения функции, нужно для избежания дублирования переходов
        int j = 0;
        Set<Integer> keyNewSet = new HashSet<>(); //Предназначен для хранения текущего множества
        // связанных переходами станций
        for(String connection : listConnections){
            keyFirst = Integer.parseInt(connection.split("[ ]")[0]);
            keySecond = Integer.parseInt(connection.split("[ ]")[1]);

            if(keySet.contains(keyFirst)||keySet.contains(keySecond)){ // Если переходы уже обрабатывались,
                // пропускаем дальнейшие действия
                continue;
            }
            findKeys(listConnections, keyFirst, keySecond, keySet, keyNewSet);

            for(int key: keyNewSet){
                incidenceMatrix[key-1][j] = true;
            }
            j++;
        }
    }

    private static void findKeys(List<String> listConnections, int keyFirst, int keySecond, Set<Integer> keySet,
                                 Set<Integer> keyNewSet) {
        keyNewSet.clear();
        keySet.add(keyFirst);
        keySet.add(keySecond);
        keyNewSet.add(keyFirst);
        keyNewSet.add(keySecond);
        for(int i = 0; i < listConnections.size() / 2; i++) { //Если внутренний цикл не повторять,
            //некоторые станции из переходов могут быть исключены, в связи с тем что поиск осуществляется
            // в направлениии слева направо
            for (String newConnection : listConnections) {
                int keyFirstNew = Integer.parseInt(newConnection.split("[ ]")[0]);
                int keySecondNew = Integer.parseInt(newConnection.split("[ ]")[1]);
                if (keyNewSet.contains(keyFirstNew) || keyNewSet.contains(keySecondNew)) {
                    keyNewSet.add(keyFirstNew);
                    keyNewSet.add(keySecondNew);
                    keySet.add(keyFirstNew);
                    keySet.add(keySecondNew);
                }
            }
        }
    }
}