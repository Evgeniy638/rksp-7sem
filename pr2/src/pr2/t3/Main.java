package pr2.t3;

public class Main {
    public static void main(String[] args) {
        IFileDataGetter generatorFile = new GeneratorFile(
                new String[]{"XML", "JSON", "XLS"},
                10, 100,
                100, 1000
        );

        FileDataHandler fileDataHandler = new FileDataHandler(generatorFile, 7, 5);

        fileDataHandler.start();
    }
}
