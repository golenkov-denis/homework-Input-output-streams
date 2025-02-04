import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static final String GAMES_DIR = "Games";
    private static final String SAVE_DIR = GAMES_DIR + "/savegames";
    private static final String TEMP_DIR = GAMES_DIR + "/temp";
    private static final String ZIP_FILE = SAVE_DIR + "/saves.zip";
    private static StringBuilder log = new StringBuilder();

    public static void main(String[] args) {
        // Выполнение задач по установке
        createDirectoriesAndFiles();

        // Выполнение задач по сохранению и архивации
        saveAndZipGames();

        // Запись лога в файл
        writeLogToFile();

        System.out.println("Программа выполнена. Результаты смотрите в файле " + TEMP_DIR + "/temp.txt");
    }

    // Методы для задачи 1 (установка)
    public static void createDirectoriesAndFiles() {
        createDirectory(GAMES_DIR);
        createDirectory(GAMES_DIR + "/src");
        createDirectory(GAMES_DIR + "/res");
        createDirectory(SAVE_DIR);
        createDirectory(TEMP_DIR);

        createDirectory(GAMES_DIR + "/src/main");
        createDirectory(GAMES_DIR + "/src/test");

        createFile(GAMES_DIR + "/src/main/Main.java");
        createFile(GAMES_DIR + "/src/main/Utils.java");

        createDirectory(GAMES_DIR + "/res/drawables");
        createDirectory(GAMES_DIR + "/res/vectors");
        createDirectory(GAMES_DIR + "/res/icons");

        createFile(TEMP_DIR + "/temp.txt");
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

    // Методы для задачи 2 (сохранение и архивация)
    public static void saveAndZipGames() {
        // 1. Создание экземпляров класса GameProgress
        GameProgress save1 = new GameProgress(100, 5, 1, 0.0);
        GameProgress save2 = new GameProgress(80, 3, 2, 15.5);
        GameProgress save3 = new GameProgress(50, 1, 3, 50.2);

        // 2. Сохранение сериализованных объектов в папку savegames
        List<String> saveFiles = new ArrayList<>();
        saveFiles.add(saveGame(SAVE_DIR + "/save1.dat", save1));
        saveFiles.add(saveGame(SAVE_DIR + "/save2.dat", save2));
        saveFiles.add(saveGame(SAVE_DIR + "/save3.dat", save3));

        // 3. Запаковка файлов сохранений в один архив zip
        zipFiles(ZIP_FILE, saveFiles);

        // 4. Удаление файлов сохранений, лежащих вне архива
        deleteSaveFiles(saveFiles);
    }

    public static String saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            log.append("Сохранение игры в файл: ").append(filePath).append("\n");
        } catch (IOException e) {
            log.append("Ошибка при сохранении игры в файл: ").append(filePath)
                    .append(": ").append(e.getMessage()).append("\n");
        }
        return filePath;
    }

    public static void zipFiles(String zipPath, List<String> filesToZip) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filesToZip) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    ZipEntry entry = new ZipEntry(filePath.substring(filePath.lastIndexOf('/') + 1));
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    log.append("Файл запакован: ").append(filePath).append("\n");
                } catch (IOException e) {
                    log.append("Ошибка при запаковке файла: ").append(filePath)
                            .append(": ").append(e.getMessage()).append("\n");
                }
            }
            log.append("Файлы запакованы в архив: ").append(zipPath).append("\n");
        } catch (IOException e) {
            log.append("Ошибка при создании ZIP архива: ").append(zipPath)
                    .append(": ").append(e.getMessage()).append("\n");
        }
    }

    public static void deleteSaveFiles(List<String> filesToDelete) {
        for (String filePath : filesToDelete) {
            File file = new File(filePath);
            if (file.delete()) {
                log.append("Файл удален: ").append(filePath).append("\n");
            } else {
                log.append("Не удалось удалить файл: ").append(filePath).append("\n");
            }
        }
    }

    private static void writeLogToFile() {
        try (FileWriter writer = new FileWriter(TEMP_DIR + "/temp.txt")) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.err.println("Ошибка записи лога: " + e.getMessage());
        }
    }

    // Класс для хранения информации об игровом процессе (должен быть Serializable)
    public static class GameProgress implements Serializable {
        private static final long serialVersionUID = 1L;

        private int health;
        private int weapons;
        private int lvl;
        private double distance;

        public GameProgress(int health, int weapons, int lvl, double distance) {
            this.health = health;
            this.weapons = weapons;
            this.lvl = lvl;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "GameProgress{" +
                    "health=" + health +
                    ", weapons=" + weapons +
                    ", lvl=" + lvl +
                    ", distance=" + distance +
                    '}';
        }
    }
}
