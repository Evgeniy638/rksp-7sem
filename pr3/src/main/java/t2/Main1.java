package t2;

import io.reactivex.rxjava3.core.Observable;

public class Main1 {
    public static void main(String[] args) {
        // Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток,
        //содержащий квадраты данных чисел.
        System.out.println("Поток квадратов 1000 случайных чисел");
        Observable.range(1, 1000)
                .map(i -> Math.round(Math.random() * 1000))
                .map(num -> Math.pow(num, 2))
                .subscribe(System.out::println);


        // Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток,
        //содержащий только числа больше 500.
        System.out.println("Поток чисел больше 500");
        Observable.range(1, 1000)
                .map(i -> Math.round(Math.random() * 1000))
                .filter(num -> num > 500)
                .subscribe(System.out::println);

        // Преобразовать поток из случайного количества (от 0 до 1000)
        //случайных чисел в поток, содержащий количество чисел.
        System.out.println("Поток количества чисел");
        Observable.range(1, (int) Math.round(Math.random() * 1000))
                .count()
                .subscribe(System.out::println);
    }
}
