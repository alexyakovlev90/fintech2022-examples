package fintech.threadpool;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ThreadPoolExample {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OneMicroSecondTask task = new OneMicroSecondTask();

        ExecutorService cachedPool = Executors.newFixedThreadPool(4);
        List<Callable<String>> tasks = IntStream.rangeClosed(1, 1_000_000)
                .mapToObj(i -> task)
                .collect(toList());

        cachedPool.invokeAll(tasks);

        long timeSpent = System.currentTimeMillis() - start;
        System.out.println("Time spent: " + timeSpent + "ms");

        cachedPool.shutdownNow();
    }

    static class OneMicroSecondTask implements Callable<String> {

        @Override
        public String call() {
            long oneMicroSecond = 1_000;
            long startedAt = System.nanoTime();
            while (System.nanoTime() - startedAt <= oneMicroSecond) ;

            return "Done";
        }
    }


}
