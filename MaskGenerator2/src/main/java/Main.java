import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String projectName = "N42";
        String studyName = "first";
        int maskNumber = 1;

        List<Layer> layerList = new ArrayList<>();
        Layer layer = new Layer();
        layer.setLayerName("RES");
        layer.setGdsNumber(101);
        layerList.add(layer);
        layer = new Layer();
        layer.setLayerName("F01");
        layer.setGdsNumber(80);
        layerList.add(layer);

        int xLeft = -8491;
        int yLeft = -8401;
        int xRight = 8401;
        int yRoght = 8491;


        String fileName = String.format("./%s_MASK%d.oas",projectName,maskNumber);
        String primary = String.format("%s_MASK%d",projectName,maskNumber);
        String results = String.format("./%s_%s_MASK%d.oas",projectName,studyName,maskNumber);



        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loaders","file");
        velocityEngine.setProperty("resource.loader.file.path","./");
        velocityEngine.setProperty("resource.loader","class");
        velocityEngine.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("template.vm", "utf-8");

        VelocityContext velocityContext = new VelocityContext(); // создание контекста Velocity
        velocityContext.put("fileName", fileName);
        velocityContext.put("primary",primary);
        velocityContext.put("results",results);
        velocityContext.put("layerList",layerList);
        velocityContext.put("xLeft",xLeft);
        velocityContext.put("xRight",xRight);
        velocityContext.put("yLeft",yLeft);
        velocityContext.put("yRight",yRoght);
        // загрузка шаблона с именем template.vm
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out)); // создается выходной поток

        template.merge(velocityContext, bw); // метод merge() принимает набор данных в виде объекта "vc" и объект потока "bw"
        bw.flush();
        bw.close();
    }


}
