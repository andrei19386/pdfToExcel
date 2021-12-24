import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите путь до папки: ");
            String path = scanner.nextLine();
            try {
                long size = FileUtils.calculateFolderSize(path);
                System.out.println("Размер папки " + path + " составляет " + FileUtils.readableSize(size));
                System.out.println("Список директорий:");
                FileUtils.printListOfDirectories(path);
                System.out.println("Список файлов:");
                FileUtils.printListOfFiles(path);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
