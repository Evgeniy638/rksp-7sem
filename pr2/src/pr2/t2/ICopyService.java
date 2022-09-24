package pr2.t2;

import java.io.IOException;

public interface ICopyService {
    void copyFile(String inputFileName, String outputFileName) throws IOException;
}
