package pr2.t1;

import java.util.concurrent.ExecutionException;

public class Main {
    private static int LENGTH_ARRAY = 10000;
    private static int MAX_VALUE = 100000;
    private static int MIN_VALUE = 0;
    private static int SLEEP_TIME = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long[] arr = generateArray();

        IMaxService maxConsistentlyService = new MaxConsistentlyService(SLEEP_TIME);
        IMaxService maxFutureService = new MaxFutureService(5, SLEEP_TIME);
        IMaxService maxForkService = new MaxForkJoinService(5, SLEEP_TIME);

        timeMaxService(maxConsistentlyService, arr, "последовательной функции F");
        timeMaxService(maxFutureService, arr, "функции F с использованием многопоточности");
        timeMaxService(maxForkService, arr, "функции F с использованием ForkJoin");
    }

    private static void timeMaxService (IMaxService maxService, long[] arr, String nameFunction) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        long result = maxService.max(arr);
        long totalTime = System.currentTimeMillis() - startTime;

        System.out.println("Время выполнения " + nameFunction + ": " + totalTime + " мс");
        System.out.println("Ответ: " + result);
        System.out.println();
    }

    private static long[] generateArray() {
        long[] arr = new long[LENGTH_ARRAY];

        for (int i = 0; i < LENGTH_ARRAY; i++) {
            arr[i] = Math.round(Math.random() * (MAX_VALUE - MIN_VALUE) + MIN_VALUE);
        }

        return arr;
    }
}
