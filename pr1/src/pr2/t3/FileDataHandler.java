package pr2.t3;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FileDataHandler {
    private final int PERIOD_MULTIPLIER;
    private final int LENGTH_QUEUE;

    private final IFileDataGetter fileDataGetter;
    private final ThreadPoolExecutor executor;

    public FileDataHandler(IFileDataGetter fileDataGetter, int PERIOD_MULTIPLIER, int LENGTH_QUEUE) {
        this.fileDataGetter = fileDataGetter;
        this.PERIOD_MULTIPLIER = PERIOD_MULTIPLIER;
        this.LENGTH_QUEUE = LENGTH_QUEUE;
        this.executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(LENGTH_QUEUE);
    }

    public void start() {
        while (true) {
            if (executor.getActiveCount() < this.LENGTH_QUEUE) {
                this.executor.submit(this.createCallable());
            }
        }
    }

    private Runnable createCallable() {
        return () -> {
            FileData fileData = this.getFileData();

            try {
                Thread.sleep((long) fileData.getSize() * this.PERIOD_MULTIPLIER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(fileData);
        };
    }

    private synchronized FileData getFileData() {
        return fileDataGetter.getFileData();
    }
}
