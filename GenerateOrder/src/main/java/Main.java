import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    private static List<Element> elements = new ArrayList<>();



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
        if(args.length==0){
            generateExecutionScript();
        }
    }

    private static void generateExecutionScript() throws IOException {
        readProperties();
        File file = new File("script_exec.sh");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        bufferedWriter.write("echo \"$(date): started\" >> log.txt &&\n");
        bufferedWriter.write("java -jar GenerateOrder-1.0-SNAPSHOT.jar 1 &&\n");
        bufferedWriter.write("echo \"$(date): first stage script generated\" >> log.txt &&\n");
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++) {
            bufferedWriter.write("calibre -drc " + ProjectInfo.getProjectName()+"_firstSTAGE"+i+".SVRF &&\n");
            bufferedWriter.write(String.format( "echo \"$(date): first stage for mask %d done\" >> log.txt &&\n",
                    i));
        }
        bufferedWriter.write("java -jar GenerateOrder-1.0-SNAPSHOT.jar 2 &&\n");
        bufferedWriter.write("echo \"$(date): second stage script generated\" >> log.txt &&\n");
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++) {
            bufferedWriter.write("calibre -drc " + ProjectInfo.getProjectName()+"_secondSTAGE"+i+".SVRF >" +
                    "log_calibri_" + i + "&&\n");
            bufferedWriter.write(String.format( "echo \"$(date): second stage for mask %d done\" >> log.txt &&\n",
                    i));
        }
        bufferedWriter.write("java -jar GenerateOrder-1.0-SNAPSHOT.jar 3 &&\n");
        bufferedWriter.write("echo \"$(date): third stage script generated\" >> log.txt\n");
        bufferedWriter.flush();
        bufferedWriter.close();
        file.setExecutable(true,false);
    }

    private static void optionThird() throws IOException {//Генерация варианта для вставки в заказ
        readProperties();
        readElements();
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
                String line = getLine(element);
                bufferedWriter.write(line);
            }
        }

        List<Element> darkCDs = elements.stream().filter(e -> e.getName().equals("CD") &&
                e.getType().equals("dark") && e.getMaskNumber()== maskNumber).collect(Collectors.toList());
        if(!darkCDs.isEmpty()){
            bufferedWriter.write("Tonality = chrome\n");
            for(Element element : darkCDs){
                String line = getLine(element);
                bufferedWriter.write(line);
            }
        }
        List<Element> lightMLTs = elements.stream().filter(e -> e.getName().equals("MLT") &&
                e.getType().equals("light") && e.getMaskNumber() == maskNumber).collect(Collectors.toList());
        if(!lightMLTs.isEmpty()){
            bufferedWriter.write("Tonality = glass\n");
            for(Element element : lightMLTs){
                String line = getLine(element);
                bufferedWriter.write(line);
            }
        }

        List<Element> darkMLTs = elements.stream().filter(e -> e.getName().equals("MLT") &&
                e.getType().equals("dark") && e.getMaskNumber() == maskNumber).collect(Collectors.toList());
        if(!darkMLTs.isEmpty()){
            bufferedWriter.write("Tonality = chrome\n");
            for(Element element : darkMLTs){
                String line = getLine(element);
                bufferedWriter.write(line);
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static String getLine(Element element) {
        double xCenter = getCenter(element.getxLeft(),element.getxRight());
        double yCenter = getCenter(element.getyBottom(),element.getyTop());
        String format="";
        NumberFormat numberFormat = new DecimalFormat("#.#####");
        switch(ProjectInfo.getOrderType()) {
            case "1": if(element.getName().equals("CD")) {
                            format = "- CD%d: (%s, %s);\n";
                            return String.format(format,element.getNumber(),numberFormat.format(xCenter),
                                    numberFormat.format(yCenter));
                         } else {
                            format = "(%s, %s),\n";
                            return String.format(format,numberFormat.format(xCenter),
                                    numberFormat.format(yCenter));
                        }
            case "2": if(element.getName().equals("CD")){
                format = " - КЛР %d: x = %s мм, y = %s мм;\n";
                return String.format(format,element.getNumber(),numberFormat.format(xCenter/1000),
                        numberFormat.format(yCenter/1000));
            } else {
                return "";
            }
        }
        return null;
    }

    private static double getCenter(double little, double big) {
        double step = 1;
        boolean isEven = false;
        while(true) {
            double center = 0.5 * (little + big);
            int centerStep = (int) (center / step);
            if(centerStep*step > little && centerStep*step < big){
                return centerStep*step;
            } else if((centerStep+1)*step > little && (centerStep+1)*step < big) {
                return (centerStep+1)*step;
            } else if((centerStep-1)*step > little && (centerStep-1)*step < big) {
                return (centerStep-1)*step;
            }else {
                    step = changeStep(step,isEven);
                    isEven = changeEven(isEven);
            }
        }
    }

    private static boolean changeEven(boolean isEven){
        if(isEven){
            return false;
        }
           return true;
    }

    private static double changeStep(double step, boolean isEven){
        if(isEven) {return step/5;}
        return step/2;
    }

    private static void readElements() {
        for(int maskNumber = 1; maskNumber <= ProjectInfo.getNumberOfMasks(); maskNumber++){
            File file = new File( "log_calibri_" + maskNumber);
            FileReader fr = null;

            try {
                fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();

                readFile(maskNumber, reader, line);
                reader.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readFile(int maskNumber, BufferedReader reader, String line) throws IOException {
        List<String> namesToBeIgnored = new ArrayList<>();
        while (line !=null){
            if(line.contains("DRC PRINT AREA") && line.contains("=")){
                line = line.replaceAll("DRC PRINT AREA","")
                        .replaceAll("=","").replaceAll("[ ]+"," ");
                String[] words = line.trim().split("[ ]");
                if(Double.parseDouble(words[1]) < 1e-10){
                    namesToBeIgnored.add(words[0]);
                }
            }
            if(line.contains("DRC PRINT EXTENT") && line.contains("=")){
                line = line.replaceAll("DRC PRINT EXTENT","")
                        .replaceAll("=","").replaceAll("[ ]+"," ");
                Element element = getElement(line, maskNumber,namesToBeIgnored);
                if(element!=null) elements.add(element);
            }
            line = reader.readLine();
        }
    }

    private static Element getElement(String line, int maskNumber,List<String> namesToBeIgnored) {
        String[] words = line.trim().split("[ ]");
        if(!namesToBeIgnored.contains(words[0])){
            Element element = new Element();
            element.setName(words[0].contains("CD")?"CD":"MLT");
            element.setType(words[0].contains("light")?"light":"dark");
            element.setMaskNumber(maskNumber);
            String toNumber = words[0].replaceAll("[a-zA-z]+","");
            element.setNumber(Integer.parseInt(toNumber));
            element.setxLeft(Double.parseDouble(words[1]));
            element.setyBottom(Double.parseDouble(words[2]));
            element.setxRight(Double.parseDouble(words[3]));
            element.setyTop(Double.parseDouble(words[4]));
            return element;
        }
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
        bufferedWriter.write("\n");
        bufferedWriter.write("DRC PRINT EXTENT " + feaureName);
        bufferedWriter.write("\n");
        bufferedWriter.write(String.format("%s {COPY %s }\n", feaureName, feaureName));
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
        rulesGenerate(bufferedWriter, ProjectInfo.getLightFieldCD(),
                "lightFieldCD","%s%d { %s%d AND RES }\n");
        rulesGenerate(bufferedWriter, ProjectInfo.getDarkFieldCD(),
                "darkFieldCD","%s%d { %s%d NOT RES }\n");
        rulesGenerate(bufferedWriter, ProjectInfo.getLightFieldMLT(),
                "lightFieldMLT","%s%d { %s%d AND RES }\n");
        rulesGenerate(bufferedWriter, ProjectInfo.getDarkFieldMLT(),
                "darkFieldMLT","%s%d { %s%d NOT RES }\n");
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static void rulesGenerate(BufferedWriter bufferedWriter, int layer, String type,
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



