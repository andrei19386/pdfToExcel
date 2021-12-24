import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static void copyFolder(String sourceDirectory, String destinationDirectory) {
        try {
            if(Path.of(destinationDirectory).toFile().exists()) {
                if(!Path.of(destinationDirectory).toFile().canRead()) {
                    throw new AccessDeniedException("The directory " + destinationDirectory + " cannot be read!");
                }
                if(!Path.of(destinationDirectory).toFile().canWrite()) {
                    throw new AccessDeniedException("The directory " + destinationDirectory + " cannot be write!");
                }
                Stream<Path> filesDestination = Files.list(Path.of(destinationDirectory));
                if (!filesDestination.collect(Collectors.toList()).isEmpty()) {
                    throw new DirectoryNotEmptyException("The target directory is not empty!");
                }
                filesDestination.close();
            }
            if(!Path.of(sourceDirectory).toFile().exists()) {
                throw new NoSuchFileException("The directory does not exists!");
            }
            if(!Path.of(sourceDirectory).toFile().canRead()) {
                throw new AccessDeniedException("The directory " + sourceDirectory + " cannot be read!");
            }
            Files.copy(Path.of(sourceDirectory), Path.of(destinationDirectory), StandardCopyOption.REPLACE_EXISTING);
            Stream<Path> files = Files.list(Path.of(sourceDirectory));
            analyzerOfStream(destinationDirectory, files);
            files.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void analyzerOfStream(String destinationDirectory, Stream<Path> files) {
        files.forEach(file -> {
            String newDestinationDirectory = destinationDirectory
                    + "\\" + file.getFileName().toString();
            if(!file.toFile().canRead()) {
                try {
                    throw new AccessDeniedException("The file or directory " +
                            file.toAbsolutePath().toString() +
                            " cannot be read!");
                } catch (AccessDeniedException e) {
                    e.printStackTrace();
                }
            }
            if(file.toFile().isDirectory()){
                copyFolder(file.toString(), newDestinationDirectory);
            } else {
                byte[] bytes = {};
                try {
                    bytes = Files.readAllBytes(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Files.write(Path.of(destinationDirectory + "\\" + file.getFileName().toString()),bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

