import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String PATH = "LAYOUT PATH \"./%s\"\n";
    public static final String PRIMARY = "LAYOUT PRIMARY \"%s\"\n";
    public static final String RESULTS_DRC ="DRC RESULTS DATABASE \"./%s\" OASIS\n";
    public static final String LAYER = "LAYER %s %d\n";
    public static final String CHECK_MAP = "DRC CHECK MAP %s %d\n";
    public static final String RENAME_CELL = "LAYOUT RENAME CELL \"%s\" \"%s\"\n";
    public static final String OASIS = "LAYOUT SYSTEM OASIS\n";
    public static final String MAX_RESULTS = "DRC MAXIMUM RESULTS ALL\n";
    public static final String MAX_VERTEX = "DRC MAXIMUM VERTEX ALL\n";

    public static void main(String[] args) throws IOException {
        if(args.length > 0 && args[0].equals("1")){
            optionFirst();
        }
        if(args.length > 0 && args[0].equals("2")){
            optionSecond();
        }
        if(args.length > 0 && args[0].equals("3")){
            optionThird();
        }
    }

    private static void optionThird() throws IOException {//Генерация варианта для вставки в заказ
        List<Element> elements;
        readProperties();
        elements = readElements();
        for(int maskNumber = 1; maskNumber <= ProjectInfo.getNumberOfMasks(); maskNumber++) {
            generateFileResultForMask(elements, maskNumber);
        }

    }

    private static void generateFileResultForMask(List<Element> elements, int maskNumber) throws IOException {
        File file = new File("Result_"+ maskNumber +".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

        List<Element> lightCDs = elements.stream().filter(e -> e.getName().equals("CD") &&
                e.getType().equals("light")
        && e.getMaskNumber()== maskNumber).collect(Collectors.toList());
        if(!lightCDs.isEmpty()){
            bufferedWriter.write("Tonality = glass\n");
            for(Element element : lightCDs){
                double xCenter = getCenter(element.getxLeft(),element.getxRight());
                double yCenter = getCenter(element.getyBottom(),element.getyTop());
                String line = getLine(xCenter,yCenter, element);
                bufferedWriter.write(line);
            }
        }

        List<Element> darkCDs = elements.stream().filter(e -> e.getName().equals("CD") &&
                e.getType().equals("dark") && e.getMaskNumber()== maskNumber).collect(Collectors.toList());
        if(!darkCDs.isEmpty()){
            bufferedWriter.write("Tonality = chrome\n");
            for(Element element : darkCDs){
                double xCenter = getCenter(element.getxLeft(),element.getxRight());
                double yCenter = getCenter(element.getyBottom(),element.getyTop());
                String line = getLine(xCenter,yCenter,element);
                bufferedWriter.write(line);
            }
        }
        List<Element> lightMLTs = elements.stream().filter(e -> e.getName().equals("MLT") &&
                e.getType().equals("light") && e.getMaskNumber() == maskNumber).collect(Collectors.toList());
        if(!lightMLTs.isEmpty()){
            bufferedWriter.write("Tonality = glass\n");
            for(Element element : lightMLTs){
                double xCenter = getCenter(element.getxLeft(),element.getxRight());
                double yCenter = getCenter(element.getyBottom(),element.getyTop());
                String line = getLine(xCenter,yCenter,element);
                bufferedWriter.write(line);
            }
        }

        List<Element> darkMLTs = elements.stream().filter(e -> e.getName().equals("MLT") &&
                e.getType().equals("dark") && e.getMaskNumber() == maskNumber).collect(Collectors.toList());
        if(!darkMLTs.isEmpty()){
            bufferedWriter.write("Tonality = chrome\n");
            for(Element element : darkMLTs){
                double xCenter = getCenter(element.getxLeft(),element.getxRight());
                double yCenter = getCenter(element.getyBottom(),element.getyTop());
                String line = getLine(xCenter,yCenter,element);
                bufferedWriter.write(line);
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static String getLine(double xCenter, double yCenter, Element element) {
        //TODO: write a method
        return null;
    }

    private static double getCenter(double little, double big) {
        double step = 1;
        while(true) {
            double center = 0.5 * (little + big);
            int centerStep = (int) (center / step);
            if(centerStep*step > little && centerStep*step < big){
                return centerStep*step;
            } else {
                step /= 2;
                //FixMe: Продумать изменение шага
            }

        }
    }

    private static List<Element> readElements() {
        //TODO: make a method
        return null;
    }

    private static void optionSecond() {//Генерация файла с координатами прямоугольников
        readProperties();
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++){
            try {
                generateSecondStageSVRF(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateSecondStageSVRF(int i) throws IOException {
        File file = new File(ProjectInfo.getProjectName()+"_secondSTAGE"+i+".SVRF");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        bufferedWriter.write(String.format(
                PATH, ProjectInfo.getProjectName()+"_firstSTAGE"+i+".oas"));
        bufferedWriter.write(String.format(
                PRIMARY, "TOP"));
        bufferedWriter.write(OASIS);
        bufferedWriter.write(String.format(
                RESULTS_DRC, ProjectInfo.getProjectName() + "_secondSTAGE"+ i + ".oas"));
        bufferedWriter.write(MAX_VERTEX);
        bufferedWriter.write(MAX_RESULTS);

        for(int j = 1; j <= 100; j++){
            String featureName = "lightCD"+j;
            int featureGDSNumber = ProjectInfo.getLightFieldCD()*100+j;
            elementaryPrint(bufferedWriter, featureName, featureGDSNumber);
            featureName = "darkCD" + j;
            featureGDSNumber = ProjectInfo.getDarkFieldCD()*100+j;
            elementaryPrint(bufferedWriter,featureName,featureGDSNumber);
            featureName = "lightMLT" + j;
            featureGDSNumber = ProjectInfo.getLightFieldMLT()*100+j;
            elementaryPrint(bufferedWriter,featureName,featureGDSNumber);
            featureName = "darkMLT" + j;
            featureGDSNumber = ProjectInfo.getDarkFieldMLT()*100+j;
            elementaryPrint(bufferedWriter,featureName,featureGDSNumber);
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static void elementaryPrint(BufferedWriter bufferedWriter, String feaureName, int feachureGDSNumber)
            throws IOException {
        bufferedWriter.write(String.format(LAYER, feaureName, feachureGDSNumber));
        bufferedWriter.write("DRC PRINT AREA " + feaureName);
        bufferedWriter.write("DRC PRINT EXTENT " + feaureName);
        bufferedWriter.write(String.format("%s {COPY %s }", feaureName, feaureName));
    }


    private static void optionFirst() {//Генерация файла со слоями-прямоугольниками
        readProperties();
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++){
            try {
                generateFirstStageSVRF(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateFirstStageSVRF(int i) throws IOException {

        File file = new File(ProjectInfo.getProjectName()+"_firstSTAGE"+i+".SVRF");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        bufferedWriter.write(String.format(
                PATH, ProjectInfo.getProjectName()+"_MASK"+i+".oas"));
        bufferedWriter.write(String.format(RENAME_CELL,
                ProjectInfo.getProjectName()+"_MASK" + i, "TOP"));
        bufferedWriter.write(String.format(
                PRIMARY, "TOP"));
        bufferedWriter.write(OASIS);
        bufferedWriter.write(String.format(
                RESULTS_DRC, ProjectInfo.getProjectName() + "_firstSTAGE"+ i + ".oas"));
        bufferedWriter.write(MAX_VERTEX);
        bufferedWriter.write(MAX_RESULTS);
        bufferedWriter.write(String.format(LAYER,"RES",ProjectInfo.getLayerOut()));
        lightFieldCDRulesGenerate(bufferedWriter, ProjectInfo.getLightFieldCD(),
                "CD","%s%d { %s%d AND RES }\n");
        lightFieldCDRulesGenerate(bufferedWriter, ProjectInfo.getDarkFieldCD(),
                "CD","%s%d { %s%d NOT RES }\n");
        lightFieldCDRulesGenerate(bufferedWriter, ProjectInfo.getLightFieldMLT(),
                "MLT","%s%d { %s%d NOT RES }\n");
        lightFieldCDRulesGenerate(bufferedWriter, ProjectInfo.getDarkFieldMLT(),
                "MLT","%s%d { %s%d NOT RES }\n");
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static void lightFieldCDRulesGenerate(BufferedWriter bufferedWriter, int layer, String type,
                                                  String format) throws IOException {
        int producedLayer = layer * 100;
        for(int j = 1; j <= 100; j++){
            bufferedWriter.write(String.format("LAYER MAP %d DATATYPE %d %d\n", layer,j, producedLayer +j));
            bufferedWriter.write(String.format(LAYER,type+j, producedLayer +j));
            bufferedWriter.write(String.format(CHECK_MAP,type+j, producedLayer +j));
            bufferedWriter.write(String.format(format,type,j,type,j));
        }
    }

    private static void readProperties() {
        try {
            File file = new File("application.properties");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                line = line.replaceAll("[ ]","");
                initializeFromFile(line);
                line = reader.readLine();
            }
            reader.close();
            fr.close();
            initializeDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeFromFile(String line) {
        if(line.contains("projectName=")){ProjectInfo.setProjectName(line
                .replaceAll("projectName=",""));}
        if(line.contains("numberOfMasks=")){
            ProjectInfo.setNumberOfMasks(Integer.parseInt(line.replaceAll("numberOfMasks=","")));
        }
        if(line.contains("layerOut=")){
            ProjectInfo.setLayerOut(Integer.parseInt(line.replaceAll("layerOut=","")));
        }
        if(line.contains("orderType=")){ProjectInfo.setOrderType(line
                .replaceAll("orderType=",""));}

        if(line.contains("lightFieldCD=")){
            ProjectInfo.setLightFieldCD(Integer.parseInt(line.replaceAll("lightFieldCD=","")));
        }

        if(line.contains("darkFieldCD=")){
            ProjectInfo.setDarkFieldCD(Integer.parseInt(line.replaceAll("darkFieldCD=","")));
        }

        if(line.contains("lightFieldMLT=")){
            ProjectInfo.setLightFieldMLT(Integer.parseInt(line.replaceAll("lightFieldMLT=","")));
        }

        if(line.contains("darkFieldMLT=")){
            ProjectInfo.setDarkFieldMLT(Integer.parseInt(line.replaceAll("darkFieldMLT=","")));
        }
    }

    private static void initializeDefault() {
        if(ProjectInfo.getLightFieldCD()==0){
            ProjectInfo.setLightFieldCD(363);
        }
        if(ProjectInfo.getDarkFieldCD()==0){
            ProjectInfo.setDarkFieldCD(364);
        }
        if(ProjectInfo.getLightFieldMLT()==0){
            ProjectInfo.setLightFieldMLT(365);
        }
        if(ProjectInfo.getDarkFieldMLT()==0){
            ProjectInfo.setDarkFieldMLT(366);
        }
    }
}



