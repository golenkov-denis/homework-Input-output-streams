import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameInstaller {
    private static StringBuilder log = new StringBuilder();

    public static void main(String[] args) {
        // Создание директорий верхнего уровня
        createDirectory("Games");
        createDirectory("Games/src");
        createDirectory("Games/res");
        createDirectory("Games/savegames");
        createDirectory("Games/temp");

        // Создание поддиректорий в src
        createDirectory("Games/src/main");
        createDirectory("Games/src/test");

        // Создание файлов в main
        createFile("Games/src/main/Main.java");
        createFile("Games/src/main/Utils.java");

        // Создание поддиректорий в res
        createDirectory("Games/res/drawables");
        createDirectory("Games/res/vectors");
        createDirectory("Games/res/icons");

        // Создание файла temp.txt и запись лога
        createFile("Games/temp/temp.txt");
        writeLogToFile();
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            log.append("Директория ").append(path).append(" создана успешно\n");
        } else {
            log.append("ОШИБКА при создании директории ").append(path).append("\n");
        }
    }

    private static void createFile(String path) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                log.append("Файл ").append(path).append(" создан успешно\n");
            } else {
                log.append("ОШИБКА при создании файла ").append(path).append("\n");
            }
        } catch (IOException e) {
            log.append("Исключение при создании файла ").append(path)
                    .append(": ").append(e.getMessage()).append("\n");
        }
    }

    private static void writeLogToFile() {
        try (FileWriter writer = new FileWriter("Games/temp/temp.txt")) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.err.println("Ошибка записи лога: " + e.getMessage());
        }
    }
}
