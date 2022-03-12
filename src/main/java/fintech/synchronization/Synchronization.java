package fintech.synchronization;


public class Synchronization {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Incrementer incrementer = new Incrementer(counter);
        Decrementer decrementer = new Decrementer(counter);

        incrementer.start();
        decrementer.start();

        incrementer.join();
        decrementer.join();

        System.out.println("Total result:" + counter.get());
    }

    public static class Decrementer extends Thread {

        private final Counter counter;

        public Decrementer(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.dec();
            }
        }
    }

    public static class Incrementer extends Thread {

        private final Counter counter;

        public Incrementer(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.inc();
            }
        }
    }

    private static class Counter {
        private int i = 0;

        Object lock = new Object();

        public void inc() {
            // logic
            synchronized (this.lock) {
                i++;
            }
        }

        public void dec() {
            synchronized (this.lock) {
                i--;
            }
        }

        public int get() {
            return i;
        }
    }


}
