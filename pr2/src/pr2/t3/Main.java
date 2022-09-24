package pr2.t3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    private static final String PATH_FILE = "src/pr2/t3/file.txt";

    public static void main(String[] args) {
        try {
            System.out.println(getChecksum(PATH_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getChecksum(String filePath) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            FileChannel fileChannel = fileInputStream.getChannel();

            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

            return getChecksum(byteBuffer);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    private static int getChecksum(ByteBuffer byteBuffer) {
        int checksum = 0;
        while (byteBuffer.hasRemaining()) {
            checksum += byteBuffer.get();
            checksum &= 0xffff; // нам важно только 16 байт
        }
        return checksum;
    }
}
