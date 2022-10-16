package t2;

import io.reactivex.rxjava3.core.Observable;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main2 {
    private static boolean isEnd = false;

    public static void main(String[] args) {
//        t1_1();
        t1_2();
//        t1_3();
    }

    //Даны два потока по 1000 элементов: первый содержит случайную
    //букву, второй — случайную цифру. Сформировать поток, каждый элемент
    //которого объединяет элементы из обоих потоков. Например, при входных
    //потоках (A, B, C) и (1, 2, 3) выходной поток — (A1, B2, B3).
    private static void t1_1() {
        Observable<String> observableString = Observable.range(1, 1000).map(i -> getRandomLetter());
        Observable<Integer> observableInteger = Observable.range(1, 1000).map(i -> getRandomNumber());

        Observable
                .zip(
                        observableString,
                        observableInteger,
                        (letter, number) -> letter + number
                )
                .subscribe(System.out::println);
    }

    //Даны два потока по 1000 элементов. Каждый содержит случайные
    //цифры. Сформировать поток, обрабатывающий оба потока последовательно.
    //Например, при входных потоках (1, 2, 3) и (4, 5, 6) выходной поток — (1, 2, 3, 4,
    //5, 6).
    private static void t1_2() {
        Observable<Integer> observable1 = Observable.range(1, 1000).map(i -> getRandomNumber());
        Observable<Integer> observable2 = Observable.range(1, 1000).map(i -> getRandomNumber());

        Observable
                .concat(observable1, observable2)
                .subscribe(System.out::println);
    }

    //Даны два потока по 1000 элементов. Каждый содержит случайные
    //цифры. Сформировать поток, обрабатывающий оба потока последовательно.
    //Например, при входных потоках (1, 2, 3) и (4, 5, 6) выходной поток — (1, 2, 3, 4,
    //5, 6).
    private static void t1_3() {
        Observable<Integer> observable1 = Observable
                .intervalRange(1, 10, 0, 50, TimeUnit.MILLISECONDS)
                .map(i -> getRandomNumber());
        Observable<String> observable2 = Observable
                .intervalRange(1, 10, 0, 50, TimeUnit.MILLISECONDS)
                .map(i -> getRandomLetter());

        Observable<Serializable> observableMain = Observable.merge(observable1, observable2);


        CountDownLatch latch = new CountDownLatch(1);

        observableMain.subscribe(System.out::println, System.out::println, latch::countDown);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomLetter() {
        String[] letters = new String[] {
          "A", "B", "C", "D", "E", "F", "G", "H"
        };
        int index = getRandomNumber(0, letters.length - 1);

        return letters[index];
    }

    private static int getRandomNumber(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    private static int getRandomNumber() {
        return getRandomNumber(0, 8);
    }
}
