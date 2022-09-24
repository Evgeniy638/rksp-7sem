package pr2.t2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FISAndFosCopyService implements ICopyService{
    private final int lengthReadingBytes;

    public FISAndFosCopyService() {
        this.lengthReadingBytes = 1024;
    }

    @Override
    public void copyFile(String inputFileName, String outputFileName) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            fileInputStream = new FileInputStream(inputFileName);
            fileOutputStream = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[this.lengthReadingBytes];
            int length;

            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
