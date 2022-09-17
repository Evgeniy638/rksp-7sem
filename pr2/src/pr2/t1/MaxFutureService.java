package pr2.t1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MaxFutureService implements IMaxService{
    private int countTask;
    private int sleepTime;

    public MaxFutureService(int countTask, int sleepTime) {
        this.countTask = countTask;
        this.sleepTime = sleepTime;
    }

    @Override
    public long max(long[] arr) throws ExecutionException, InterruptedException {
        FutureTask<Long>[] tasks = new FutureTask[countTask];

        int lengthInterval = (int) Math.ceil(arr.length / countTask);

        for (int i = 0; i < tasks.length; i++) {
            int startIndex = i * lengthInterval;
            int endIndex = Math.min(arr.length, (i + 1) * lengthInterval);

            tasks[i] = new FutureTask<>(createTask(arr, startIndex, endIndex));
        }

        for (int i = 0; i < tasks.length; i++) {
            new Thread(tasks[i]).start();
        }

        long max = Long.MIN_VALUE;

        for (int i = 0; i < tasks.length; i++) {
            long maxTask = tasks[i].get();

            if (maxTask > max) {
                max = maxTask;
            }

            Thread.sleep(this.sleepTime); // для более явных замеров
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

                Thread.sleep(this.sleepTime); // для более явных замеров
            }

            return max;
        };
    }
}
