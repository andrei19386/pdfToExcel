import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final String LAYER = "LAYER %s %d\n";
    public static final String CHECK_MAP = "DRC CHECK MAP %s %d\n";


    private static List<Element> elements = new ArrayList<>();


    public  static String command;

    public  static String extension;

    public  static String execExtension;

    public  static String folder;

    public  static String delCommand;

    public  static String extentTemplate;

    public static String prefix;

    public static boolean isWrittingToOutStrim;

    public static final String OUT_DIR = "";
    public static final int DIGITS_GDS = 2;

    private static void initialize() {
        if(ProjectInfo.getProgramType()==null){
            command = "klayout -b -r";
            extension = ".lydrc";
            execExtension = ".sh";
            folder = "klayout_linux";
            extentTemplate = "bbox= ";
            prefix="";
            delCommand = "rm";
            isWrittingToOutStrim = false;
            return;
        }
        switch (ProjectInfo.getProgramType()) {
            case "calibri": {
                command = "calibre -drc";
                extension = ".svrf";
                execExtension = ".sh";
                folder = "calibri";
                extentTemplate = "DRC PRINT EXTENT trimBoundary = ";
                prefix="";
                delCommand = "rm";
                isWrittingToOutStrim = true;
                break;
            }
            case "klayout_windows": {
                command = "\"c:\\Users\\glushko_aa\\AppData\\Roaming\\KLayout (64bit)\\kLayout_app.exe\" -b -r";
                extension = ".lydrc";
                execExtension = ".bat";
                folder = "klayout_windows";
                extentTemplate = "bbox= ";
                prefix=System.getProperty("user.dir")+"\\"+ OUT_DIR;
                prefix = prefix.replaceAll("\\\\","\\\\\\\\");
                isWrittingToOutStrim = false;
                delCommand = "cmd /c del";
                break;
            }
            case "klayout_linux": {
                command = "klayout -b -r";
                extension = ".lydrc";
                execExtension = ".sh";
                folder = "klayout_linux";
                extentTemplate = "bbox= ";
                prefix="";
                delCommand = "rm";
                isWrittingToOutStrim = false;
                break;

            }
            default: {
                command = "";
                extension = "";
                folder = "";
                extentTemplate = "";
                prefix="";
                isWrittingToOutStrim = false;
            }

        }
    }

    public static void main(String[] args) throws Exception {
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


    private static Template getTemplateScript(String templateFile) throws Exception {
        VelocityEngine velocityEngine = getVelocityEngine();
        Template template = velocityEngine.getTemplate(templateFile, "utf-8");
        return template;
    }


    private static VelocityEngine getVelocityEngine() throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loaders","file");
        velocityEngine.setProperty("resource.loader.file.path","./" );
        velocityEngine.setProperty("resource.loader","class");
        velocityEngine.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();
        return velocityEngine;
    }
    private static void generateExecutionScript() throws Exception {
        readProperties();
        File file = new File("script_exec.sh");

        Template template = getTemplateScript(folder +  System.getProperty("file.separator")
                + "template_script_exec.vm");
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("version", ProjectInfo.getVersion());
        velocityContext.put("project",ProjectInfo.getProjectName());
        velocityContext.put("numberOfMasks",ProjectInfo.getNumberOfMasks());
        velocityContext.put("command",command);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        template.merge(velocityContext, bufferedWriter);

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
            File file = new File( ProjectInfo.getProjectName() + "_log_" + maskNumber + ".txt");
            FileReader fr = null;

            try {
                fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                if(isWrittingToOutStrim) {
                    readFile(maskNumber, reader, line);
                } else {
                    readLogFile(maskNumber,reader,line);
                }
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

    private static void readLogFile(int maskNumber, BufferedReader reader, String line) throws IOException {
        List<String> namesToBeIgnored = new ArrayList<>();
        while (line !=null){
            if(line.contains("area") && line.contains("=")){
                line = line.replaceAll("area","")
                        .replaceAll("=","").replaceAll("[ ]+"," ");
                String[] words = line.trim().split("[ ]");
                if(Double.parseDouble(words[1]) < 1e-10){
                    namesToBeIgnored.add(words[0]);
                }
            }
            if(line.contains("bbox") && line.contains("=")){
                line = line.replaceAll("bbox","")
                        .replaceAll("=","").replaceAll("[ ]+"," ");
                Element element = null;
                if(isWrittingToOutStrim) {
                    element = getElement(line, maskNumber, namesToBeIgnored);
                } else {
                    element = getElementVer2(line,maskNumber,namesToBeIgnored);
                }
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

    private static Element getElementVer2(String line, int maskNumber,List<String> namesToBeIgnored) {
        String[] words = line.trim().split("[ ]");
        if(!namesToBeIgnored.contains(words[0])){
            Element element = new Element();
            element.setName(words[0].contains("CD")?"CD":"MLT");
            element.setType(words[0].contains("light")?"light":"dark");
            element.setMaskNumber(maskNumber);
            String toNumber = words[0].replaceAll("[a-zA-z]+","");
            element.setNumber(Integer.parseInt(toNumber));
            String[] words1 = words[1].replaceAll(";",",")
                    .replaceAll("[(]","").replaceAll("[)]","")
                    .split(",");
            element.setxLeft(Double.parseDouble(words1[0]));
            element.setyBottom(Double.parseDouble(words1[1]));
            element.setxRight(Double.parseDouble(words1[2]));
            element.setyTop(Double.parseDouble(words1[3]));
            return element;
        }
        return null;
    }

    private static void optionSecond() throws Exception {//Генерация файла с координатами прямоугольников
        readProperties();
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++){
            try {
                generateSecondStageSVRF(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateSecondStageSVRF(int i) throws Exception {
        File file = new File(ProjectInfo.getProjectName()+"_secondSTAGE"+i+extension);


        Template template = getTemplateScript(folder +  System.getProperty("file.separator")
                + "secondSTAGE_template.vm");
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("fileName", prefix + ProjectInfo.getProjectName()+"_firstSTAGE"+i+".oas");
        velocityContext.put("result",prefix + ProjectInfo.getProjectName() + "_secondSTAGE"+ i + ".oas");
        velocityContext.put("lightCD",ProjectInfo.getLightFieldCD());
        velocityContext.put("darkCD",ProjectInfo.getDarkFieldCD());
        velocityContext.put("lightMLT",ProjectInfo.getLightFieldMLT());
        velocityContext.put("darkMLT",ProjectInfo.getDarkFieldMLT());
        velocityContext.put("log",prefix + ProjectInfo.getProjectName()+"_log_"+i+".txt");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        template.merge(velocityContext, bufferedWriter);

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


    private static void optionFirst() throws Exception {//Генерация файла со слоями-прямоугольниками
        readProperties();
        for(int i = 1; i <= ProjectInfo.getNumberOfMasks();i++){
            try {
                generateFirstStageSVRF(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateFirstStageSVRF(int i) throws Exception {

        File file = new File(ProjectInfo.getProjectName()+"_firstSTAGE" + i + extension);
        Template template = getTemplateScript(folder +  System.getProperty("file.separator")
                + "firstSTAGE_template.vm");
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("fileName", prefix + ProjectInfo.getProjectName()+"_MASK"+i+".oas");
        velocityContext.put("primary", ProjectInfo.getProjectName()+"_MASK" + i);
        velocityContext.put("result", prefix + ProjectInfo.getProjectName() + "_firstSTAGE"+ i + ".oas");
        velocityContext.put("gdsRes",ProjectInfo.getLayerOut());
        velocityContext.put("lightCD",ProjectInfo.getLightFieldCD());
        velocityContext.put("darkCD",ProjectInfo.getDarkFieldCD());
        velocityContext.put("lightMLT",ProjectInfo.getLightFieldMLT());
        velocityContext.put("darkMLT",ProjectInfo.getDarkFieldMLT());

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        template.merge(velocityContext, bufferedWriter);

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
            initialize();
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
        if(line.contains("version=")){
            ProjectInfo.setVersion(line.replaceAll("version=",""));
        }

        if(line.contains("programType=")){ProjectInfo.setProgramType(line
                .replaceAll("programType=",""));}
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



