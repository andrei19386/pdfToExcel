import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        try {
            String url = "https://lenta.ru";
            List<String> imageAddresses = getImagesAddresses(url);
            Set<String> imageAddressesFiltered = filterNames(imageAddresses);
            imageAddressesFiltered.forEach(System.out::println);
            getImages(imageAddressesFiltered);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
      }



 public static String getHtmlFile(String url) throws IOException {
        return Jsoup.connect(url).get().toString();
 }

 public static List<String> getImagesAddresses(String url) throws IOException {
     List<String> elements = new ArrayList<>();
     Document doc = Jsoup.connect(url).get();
     Elements newsHeadlines = doc.select("img");
     for (Element headline : newsHeadlines) {
            String absSrc = headline.attr("abs:src");
            elements.add(absSrc);
     }
     return elements;
 }

 public static Set<String> filterNames(List<String> imageAddresses) {
     Set<String> elements = new HashSet<>();
     for(String imageAddress : imageAddresses) {
         String name = getFileName(imageAddress);
         if(name.matches("[A-z_0-9]+[.][A-z0-9]{1,4}")) {
               elements.add(imageAddress);
         }
     }
     return elements;
 }

public static void getImages(Set<String> imageAddressesFiltered) throws Exception {

   for (String imageAddress : imageAddressesFiltered) {
       URL url = new URL(imageAddress);
       BufferedImage image = ImageIO.read(url);
       String name = getFileName(imageAddress);
       String formatName = name.split("[.]")[1];
       ImageIO.write(image, formatName,new File("images/" + name));

   }
}

    private static String getFileName(String imageAddress) {
        String[] addressParties = imageAddress.split("[/]");
        return addressParties[addressParties.length-1];
    }

}


