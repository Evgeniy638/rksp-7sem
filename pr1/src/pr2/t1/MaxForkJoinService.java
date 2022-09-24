package pr2.t1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class MaxForkJoinService implements IMaxService{
    private int countTask;
    private int sleepTime;

    public MaxForkJoinService(int countTask, int sleepTime) {
        this.countTask = countTask;
        this.sleepTime = sleepTime;
    }

    @Override
    public long max(long[] arr) throws InterruptedException {
        RecursiveTask<Long>[] tasks = new RecursiveTask[countTask];

        int lengthInterval = (int) Math.ceil((float) arr.length / countTask);

        for (int i = 0; i < countTask; i++) {
            int startIndex = i * lengthInterval;
            int endIndex = Math.min(arr.length, (i + 1) * lengthInterval);

            tasks[i] = createTask(arr, startIndex, endIndex);
            tasks[i].fork();
        }
        long max = Long.MIN_VALUE;

        for (RecursiveTask<Long> task : tasks) {
            long maxTask = task.join();

            if (maxTask > max) {
                max = maxTask;
            }

            Thread.sleep(sleepTime); // для более явных замеров
        }

        return max;
    }

    private RecursiveTask<Long> createTask(long[] arr, int startIndex, int endIndex) {
        return new RecursiveTask<>() {
            @Override
            protected Long compute() {
                long max = arr[startIndex];

                for (int i = startIndex + 1; i < endIndex; i++) {
                    if (arr[i] > max) {
                        max = arr[i];
                    }

                    try {
                        Thread.sleep(sleepTime); // для более явных замеров
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return max;
            }
        };
    }
}
