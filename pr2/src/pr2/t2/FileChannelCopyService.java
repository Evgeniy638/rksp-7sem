package pr2.t2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelCopyService implements ICopyService {
    @Override
    public void copyFile(String inputFileName, String outputFileName) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(inputFileName).getChannel();
            outputChannel = new FileOutputStream(outputFileName).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }finally{
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }
}
