package pr2.t2;

import java.io.IOException;

public class Main {
    private static final String PATH_FOLDER = "src/pr2/t2/";

    public static void main(String[] args) throws IOException {
        FileGenerator.main(args);

        ICopyService fisAndFosCopyService = new FISAndFosCopyService();
        ICopyService fileChannelCopyService = new FileChannelCopyService();
        ICopyService apacheCopyService = new ApacheCopyService();
        ICopyService filesClassCopyService = new FilesClassCopyService();

        timeCopyService(
                fisAndFosCopyService,
                FileGenerator.FILENAME,
                PATH_FOLDER + "file_t2_copy_fisAndFos.txt",
                "FileInputStream/FileOutputStream"
        );

        timeCopyService(
                fileChannelCopyService,
                FileGenerator.FILENAME,
                PATH_FOLDER + "file_t2_copy_fileChannel.txt",
                "FileChannel"
        );

        timeCopyService(
                apacheCopyService,
                FileGenerator.FILENAME,
                PATH_FOLDER + "file_t2_copy_apacheCommonsIO.txt",
                "Apache Commons IO"
        );

        timeCopyService(
                filesClassCopyService,
                FileGenerator.FILENAME,
                PATH_FOLDER + "file_t2_copy_FilesClass.txt",
                "Files class"
        );
    }

    private static void timeCopyService (ICopyService copyService, String inputFileName, String outputFileName, String nameFunction) throws IOException {
        long startTime = System.currentTimeMillis();
        copyService.copyFile(inputFileName, outputFileName);
        long totalTime = System.currentTimeMillis() - startTime;

        System.out.println("Время выполнения " + nameFunction + ": " + totalTime + " мс");
    }
}
