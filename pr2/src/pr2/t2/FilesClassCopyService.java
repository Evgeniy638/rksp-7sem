package pr2.t2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesClassCopyService implements ICopyService {
    @Override
    public void copyFile(String inputFileName, String outputFileName) throws IOException {
        Path inputPath = Paths.get(inputFileName);
        Path outputPath = Paths.get(outputFileName);

        Files.copy(inputPath, outputPath);
    }
}
