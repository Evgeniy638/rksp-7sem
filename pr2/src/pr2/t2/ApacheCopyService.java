package pr2.t2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ApacheCopyService implements ICopyService {
    @Override
    public void copyFile(String inputFileName, String outputFileName) throws IOException {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        FileUtils.copyFile(inputFile, outputFile);
    }
}
