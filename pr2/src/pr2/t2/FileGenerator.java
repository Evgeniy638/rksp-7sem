package pr2.t2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileGenerator {
    public static final String FILENAME = "src/pr2/t2/file_t2.txt";
    public static final long SIZE_IN_BYTES = 100 * 1024 * 1024;

    public static void main(String[] args) throws IOException {
        File file = new File(FILENAME);
        file.createNewFile();

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.writeUTF("Hello world!");
        raf.setLength(SIZE_IN_BYTES);
        raf.close();
    }
}
