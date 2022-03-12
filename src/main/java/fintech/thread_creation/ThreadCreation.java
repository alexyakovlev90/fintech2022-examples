package fintech.thread_creation;

public class ThreadCreation {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //inside thread
                System.out.println("1 – " + Thread.currentThread().getName());
            }
        });

        thread.setName("not main");

        // before starting a new thread
        System.out.println("2 – " + Thread.currentThread().getName());
        thread.start();
        thread.join();
        // after starting a new thread
        System.out.println("3 – " + Thread.currentThread().getName());
    }
}
