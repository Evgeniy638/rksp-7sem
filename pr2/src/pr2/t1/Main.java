package pr2.t1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String PATH_FILE = "src/pr2/t1/file.txt";

    public static void main(String[] args) {
        Path path = Paths.get(PATH_FILE);

        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){

            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                System.out.println(currentLine);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
