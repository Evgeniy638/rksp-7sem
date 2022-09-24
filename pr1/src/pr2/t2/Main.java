package pr2.t2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    private static final int MIN_DELAY = 1000;
    private static final int MAX_DELAY = 5000;

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        ArrayList<FutureTask<String>> tasks = new ArrayList<>();

        while (true) {
            if (System.in.available() != 0 && scanner.hasNextInt()) {
                int number = scanner.nextInt();
                FutureTask<String> futureTask = new FutureTask<>(createCallable(number));

                tasks.add(futureTask);
                new Thread(futureTask).start();
            } else {
                callbackFutureTasksIfDone(tasks);
            }
        }
    }

    private static void callbackFutureTasksIfDone (ArrayList<FutureTask<String>> tasks) throws ExecutionException, InterruptedException {
//        for (int i = tasks.size() - 1; i >= 0; i--) {
//            if (tasks.get(i).isDone()) {
//                FutureTask<String> futureTask = tasks.remove(i);
//
//                System.out.println(futureTask.get());
//            }
//        }

        if (!tasks.isEmpty() && tasks.get(0).isDone()) {
            FutureTask<String> futureTask = tasks.remove(0);

            System.out.println(futureTask.get());
        }
    }

    private static Callable<String> createCallable(int number) {
        return () -> {
            try {
                Thread.sleep(generateDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double result = Math.pow(number, 2);

            return "Квадрат числа " + number + " равен: " + result;
        };
    }

    private static int generateDelay() {
        return (int) Math.round(MIN_DELAY + (MAX_DELAY - MIN_DELAY) * Math.random());
    }
}
