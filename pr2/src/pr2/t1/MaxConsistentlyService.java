package pr2.t1;

public class MaxConsistentlyService implements IMaxService {
    private int sleepTime;

    public MaxConsistentlyService(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public long max(long[] arr) throws InterruptedException {
        long max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }

            Thread.sleep(sleepTime); // для более явных замеров
        }

        return max;
    }
}
