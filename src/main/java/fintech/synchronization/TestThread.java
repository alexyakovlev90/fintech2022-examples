package fintech.synchronization;

public class TestThread extends Thread {

    Thread m;

    TestThread(Thread m) {
        this.m = m;
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        try {
            m.join();
        } catch (InterruptedException e) {
        }
        System.out.println(t.getName() + ": " + m.getName() + " isAlive=" + m.isAlive() + " State is: " + m.getState());
        System.out.println("Last line of child thread");

    }

    public static void main(String[] args) throws InterruptedException {
        Thread m = Thread.currentThread();
        new TestThread(m).start();
        System.out.println("Last line of main");

        System.exit(1);
    }
}
