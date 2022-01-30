import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static int newWidth = 300;

    public static void main(String[] args) {
        String srcFolder = "source";
        String dstFolder = "dst";

        File srcDir = new File(srcFolder);
        long start = System.currentTimeMillis();
        File[] files = srcDir.listFiles();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int arrayLength = files.length / numberOfThreads;
        int numberOfCopiedOnFirstThreads = arrayLength*(numberOfThreads - 1);


        for(int i = 0; i < numberOfThreads - 1; i++) {
            File[] partFiles = new File[arrayLength];
            System.arraycopy(files,arrayLength*i, partFiles,0,arrayLength);
            ImageResizer imageResizer = new ImageResizer(partFiles,dstFolder,newWidth,start);
            new Thread(imageResizer).start();
        }

        File[] leftFiles = new File[files.length - numberOfCopiedOnFirstThreads];

        System.arraycopy(files,numberOfCopiedOnFirstThreads, leftFiles,0,
                files.length - numberOfCopiedOnFirstThreads);
        ImageResizer imageResizerEnd = new ImageResizer(leftFiles,dstFolder,newWidth,start);
        new Thread(imageResizerEnd).start();
    }
}
