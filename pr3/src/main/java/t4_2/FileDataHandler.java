package t4_2;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FileDataHandler {
    private final int PERIOD_MULTIPLIER;
    private final String TYPE_FILE;

    private final IFileDataGetter fileDataGetter;
    private final ThreadPoolExecutor executor;
    private final int nThreads;

    public FileDataHandler(IFileDataGetter fileDataGetter, int PERIOD_MULTIPLIER, String TYPE_FILE, int nThreads) {
        this.fileDataGetter = fileDataGetter;
        this.PERIOD_MULTIPLIER = PERIOD_MULTIPLIER;
        this.TYPE_FILE = TYPE_FILE;
        this.executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
        this.nThreads = nThreads;
    }

    public void start() {
        while (true) {
            if (executor.getActiveCount() < this.nThreads) {
                this.executor.submit(this.createCallable());
            }
        }
    }

    private Runnable createCallable() {
        return () -> {
            FileData fileData = fileDataGetter.getFileData();

            if (!fileData.getType().equals(this.TYPE_FILE)) {
                return;
            }

            try {
                Thread.sleep((long) fileData.getSize() * this.PERIOD_MULTIPLIER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(fileData);
        };
    }
}
