import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите директорию, которую надо скопировать:");
        String sourceDirectory = scanner.nextLine();
        if(!Path.of(sourceDirectory).toFile().exists()) {
            System.out.println("Директория не существует!");
            return;
        }
        System.out.println("Введите директорию назначения:");
        String destinationDirectory = scanner.nextLine();
        FileUtils.copyFolder(sourceDirectory,destinationDirectory);
    }
}
