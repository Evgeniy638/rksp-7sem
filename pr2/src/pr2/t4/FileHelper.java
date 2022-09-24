package pr2.t4;

import pr2.t2.FISAndFosCopyService;
import pr2.t2.ICopyService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {
    private static final String PLUS_SIGN = "+";
    private static final String MINUS_SIGN = "-";
    private static final ICopyService copyService = new FISAndFosCopyService();

    public static long getSize(String fileName) throws IOException {
        return Files.size(Paths.get(fileName));
    }

    public static boolean isDirectory(String fileName) {
        return Files.isDirectory(Paths.get(fileName));
    }

    public static int getChecksum(String fileName) throws IOException {
        if (isDirectory(fileName)) {
            return -1;
        }

        return pr2.t3.Main.getChecksum(fileName);
    }

    private static void copyFile(String inputFileName, String outputFileName) throws IOException {
        copyService.copyFile(inputFileName, outputFileName);
    }

    public static boolean deleteFile(String fileName) throws IOException {
        File fileOrDirectory = new File(fileName);

        return deleteFile(fileOrDirectory);
    }

    private static boolean deleteFile(File file) throws IOException {
        File[] allContents = file.listFiles();
        if (allContents != null) {
            for (File childFile : allContents) {
                deleteFile(childFile);
            }
        }
        return file.delete();
    }

    public static void copyFileOrFolder(String inputFolder, String outputFolder) throws IOException {
        File source = new File(inputFolder);
        File destination = new File(outputFolder);

        copyFileOrFolder(source, destination);
    }

    private static void copyFileOrFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            for (String f : Objects.requireNonNull(source.list())) {
                copyFileOrFolder(new File(source, f), new File(destination, f));
            }
        } else {
            copyFile(source.getPath(), destination.getPath());
        }
    }

    public static ArrayList<String> diff(String newFile, String oldFile) throws IOException {
        List<String> newFileLines = readAllLines(newFile);
        List<String> oldFileLines = readAllLines(oldFile);

        List<Row> addedRows = diffFirstBetweenSecond(newFileLines, oldFileLines, PLUS_SIGN);
        List<Row> deletedRows = diffFirstBetweenSecond(oldFileLines, newFileLines, MINUS_SIGN);

        ArrayList<Row> addedAndDeletedRows = new ArrayList<>();
        addedAndDeletedRows.addAll(addedRows);
        addedAndDeletedRows.addAll(deletedRows);

        addedAndDeletedRows.sort((row1, row2) -> {
            int diff = row1.getNumber() - row2.getNumber();

            if (row1.getSign().equals(row2.getSign()) || diff != 0) {
                return diff;
            }

            if (row1.getSign().equals(PLUS_SIGN)) return 1;

            return -1;
        });

        return (ArrayList<String>) addedAndDeletedRows
                .stream()
                .map((row -> row.getSign() + row.getNumber() + "\t" + row.getString()))
                .collect(Collectors.toList());
    }

    /**
     * @return Массив строк которые есть в первом, но нет во втором списке
     */
    private static List<Row> diffFirstBetweenSecond(List<String> firstList, List<String> secondList, String sign) {
        List<Row> rows = new ArrayList<>();

        for (int i = 0; i < firstList.size(); i++) {
            if (!secondList.contains(firstList.get(i))) {
                rows.add(new Row(i + 1, firstList.get(i), sign));
            }
        }

        return rows;
    }

    private static List<String> readAllLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }

    static class Row {
        private int number;
        private String string;
        private String sign;

        public Row(int number, String string, String sign) {
            this.number = number;
            this.string = string;
            this.sign = sign;
        }

        public int getNumber() {
            return number;
        }

        public String getString() {
            return string;
        }

        public String getSign() {
            return sign;
        }
    }
}
