package pr2.t1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MaxForkJoinService implements IMaxService{
    private int countTask;
    private int sleepTime;

    public MaxForkJoinService(int countTask, int sleepTime) {
        this.countTask = countTask;
        this.sleepTime = sleepTime;
    }

    @Override
    public long max(long[] arr) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newWorkStealingPool();
        Future<Long>[] tasks = new Future[countTask];

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        int lengthInterval = (int) Math.ceil((float) arr.length / countTask);

        for (int i = 0; i < countTask; i++) {
            int startIndex = i * lengthInterval;
            int endIndex = Math.min(arr.length, (i + 1) * lengthInterval);

            tasks[i] = forkJoinPool.submit(createTask(arr, startIndex, endIndex));
        }
        long max = Long.MIN_VALUE;

        for (Future<Long> task : tasks) {
            long maxTask = task.get();

            if (maxTask > max) {
                max = maxTask;
            }

            Thread.sleep(sleepTime); // для более явных замеров
        }

        return max;
    }

    private Callable<Long> createTask(long[] arr, int startIndex, int endIndex) {
        return () -> {
            long max = arr[startIndex];

            for (int i = startIndex + 1; i < endIndex; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }

                Thread.sleep(sleepTime); // для более явных замеров
            }

            return max;
        };
    }
}
