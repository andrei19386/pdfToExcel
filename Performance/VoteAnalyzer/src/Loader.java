import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;


public class Loader {


    public static void main(String[] args) throws Exception {
        String fileName = "res/data-1572M.xml";
        long time = System.currentTimeMillis();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();
        parser.parse(new File(fileName), handler);

        handler.printDuplicatedVoters();


        time = System.currentTimeMillis() - time;
        System.out.println("Time = " + time);
    }
}