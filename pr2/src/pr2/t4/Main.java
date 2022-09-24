package pr2.t4;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Main {
    private static final String PATH_FOLDER = "src/pr2/t4/folder";
    private static final String PATH_SHADOW_FOLDER = "src/pr2/t4/shadow_folder";

    public static void main(String[] args) throws IOException, InterruptedException {
        initShadowFolder();

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(PATH_FOLDER);
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        );

        boolean poll = true;

        while (poll) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (StandardWatchEventKinds.ENTRY_CREATE.equals(kind)) {
                    createHandler(event);
                } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind)) {
                    modifyHandler(event);
                } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
                    deleteHandler(event);
                }
            }

            poll = key.reset();
        }
    }

    private static void createHandler(WatchEvent<?> event) throws IOException {
        System.out.println("Новый файл: " + getFileName(event));
        System.out.println();

        FileHelper.copyFileOrFolder(getPathToFile(event), getPathToShadowFile(event));
    }

    private static void modifyHandler(WatchEvent<?> event) throws IOException {
        String pathToFile = getPathToFile(event);
        String pathToShadowFile = getPathToShadowFile(event);


        if (FileHelper.isDirectory(getPathToFile(event))) {
            System.out.println("Изменённый директория: " + getFileName(event));
        } else {
            System.out.println("Изменённая файл: " + getFileName(event));

            for (String diffRow: FileHelper.diff(pathToFile, pathToShadowFile)) {
                System.out.println(diffRow);
            }
        }

        System.out.println();

        FileHelper.copyFileOrFolder(pathToFile, pathToShadowFile);
    }

    private static void deleteHandler(WatchEvent<?> event) throws IOException {
        String fileName = getFileName(event);
        int checksum = FileHelper.getChecksum(getPathToShadowFile(event));
        long size = FileHelper.getSize(getPathToShadowFile(event));

        System.out.println("Удалён файл " + fileName + ", размер - " + size + ", контрольная сумма - " + checksum);
        System.out.println();

        FileHelper.deleteFile(getPathToShadowFile(event));
    }

    private static void initShadowFolder() throws IOException {
        FileHelper.copyFileOrFolder(PATH_FOLDER, PATH_SHADOW_FOLDER);
    }

    private static String getFileName(WatchEvent<?> event) {
        return event.context().toString();
    }

    private static String getPathToFile(WatchEvent<?> event) {
        return PATH_FOLDER + "/" + getFileName(event);
    }

    private static String getPathToShadowFile(WatchEvent<?> event) {
        return PATH_SHADOW_FOLDER + "/" + getFileName(event);
    }
}
