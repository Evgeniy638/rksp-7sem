package pr2.t1;

import java.util.concurrent.ExecutionException;

public interface IMaxService {
    long max(long[] arr) throws InterruptedException, ExecutionException;
}
