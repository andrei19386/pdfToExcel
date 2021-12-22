import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public final static long BYTES_IN_KBYTES = 1024L;
    public final static long BYTES_IN_MBYTES = BYTES_IN_KBYTES * 1024;
    public final static long BYTES_IN_GBYTES = BYTES_IN_MBYTES * 1024;

    public static String readableSize(long size){
        String sizeString = "";
        if(size > BYTES_IN_GBYTES){

            sizeString = Double.toString((100 * size / BYTES_IN_GBYTES)*0.01);
            sizeString += " Гб";
            return sizeString;
        }
        if(size > BYTES_IN_MBYTES){
            sizeString = Double.toString((100 * size / BYTES_IN_MBYTES)*0.01);
            sizeString += " Мб";
            return sizeString;
        }
        if(size > BYTES_IN_KBYTES){
            sizeString = Double.toString((100 * size / BYTES_IN_KBYTES)*0.01);
            sizeString += " Кб";
            return sizeString;
        }
        sizeString = Long.toString(size) + " байт";
        return sizeString;
    }

    public static long calculateFolderSize(String path) {
        long currentItemSize = 0L;
        if(!Files.exists(Path.of(path))) {
            throw new IllegalArgumentException("The Directory " + path + " does not exist!");
        }
        if(!Files.isDirectory(Path.of(path))){

            throw new IllegalArgumentException("This File " + path + " is not a directory!");
        }

        try {
            if(!Files.isReadable(Path.of(path))){
                  throw new IllegalAccessException("The directory " + " is not readable!");
            }
            Stream<Path> lines = Files.list(Path.of(path));
            for (Path line : lines.collect(Collectors.toList())) {
                if (!Files.isReadable(line)) {
                    throw new IllegalAccessException("The file or directory " + line.toString() + " is not readable!");
                }
                currentItemSize = getSize(currentItemSize, line);
            }
            lines.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return currentItemSize;
    }

    private static long getSize(long currentItemSize, Path line) {
        try {
            if(!Files.isReadable(line)){
                throw new IllegalAccessException("The file or directory " + line.toString() +
                        " is not readable!");
            }
            if (Files.isDirectory(line)) {
                Stream<Path> linesSubLevel = Files.list(line);
                for (Path subLine : linesSubLevel.collect(Collectors.toList())) {
                    if (!Files.isReadable(subLine)) {
                        throw new IllegalAccessException("The file or directory " + subLine.toString() +
                                " is not readable!");
                    }
                        currentItemSize += subLine.toFile().length();
                }
                linesSubLevel.close();
            } else {
                currentItemSize += line.toFile().length();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return currentItemSize;
    }
}
