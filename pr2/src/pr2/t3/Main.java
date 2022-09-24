package pr2.t3;

import jdk.internal.ref.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static int getChecksum(String filePath) throws IOException {
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        MappedByteBuffer byteBuffer = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            fileChannel = fileInputStream.getChannel();
            byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

            return getChecksum(byteBuffer);
        } finally {
            if (byteBuffer != null) {
                System.out.println("close byteBuffer");
                // В java баг https://bugs.java.com/bugdatabase/view_bug.do?bug_id=4715154
                // удаляем вручную)
                Class<?> unsafeClass = null;
                try {
                    unsafeClass = Class.forName("sun.misc.Unsafe");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Field unsafeField = null;
                try {
                    unsafeField = unsafeClass != null ? unsafeClass.getDeclaredField("theUnsafe") : null;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if (unsafeField != null) {
                    unsafeField.setAccessible(true);
                }
                Object unsafe = null;
                try {
                    unsafe = unsafeField.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Method invokeCleaner = null;
                try {
                    if (unsafeClass != null) {
                        invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    if (invokeCleaner != null) {
                        invokeCleaner.invoke(unsafe, byteBuffer);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            if (fileChannel != null) {
                fileChannel.close();
            }

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
