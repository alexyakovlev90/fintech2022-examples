package fintech.loom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoomExample {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger num = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000_000; i++) {
//            Thread thread = Thread.startVirtualThread(() -> num.addAndGet(1));
            Thread thread = new Thread(() -> num.addAndGet(1));
            thread.start();

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long stop = System.currentTimeMillis();

        System.out.println("Time spent: " + (stop - start));
    }

}
