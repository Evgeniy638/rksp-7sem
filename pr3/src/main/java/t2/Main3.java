package t2;

import io.reactivex.rxjava3.core.Observable;

public class Main3 {
    //Дан поток из 10 случайных чисел. Сформировать поток, содержащий
    //только первые 5 чисел.
    public static void main(String[] args) {
        Observable.range(1, 10)
                .map(i -> getRandomNumber(0, 10))
                .take(5)
                .subscribe(System.out::println);
    }

    private static int getRandomNumber(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}
