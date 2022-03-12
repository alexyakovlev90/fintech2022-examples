package fintech.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockCureExample {
    private final Lock lock1 = new ReentrantLock(true);
    private final Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        DeadlockCureExample deadlockCureExample = new DeadlockCureExample();
        new Thread(deadlockCureExample::operation1, "T1").start();
        new Thread(deadlockCureExample::operation2, "T2").start();
    }

    public void operation1() {
        while (true) {
            tryLock(lock1, 50);
            System.out.println("lock1 acquired, trying to acquire lock2.");
            sleep(50);

            if (lock2.tryLock()) {
                System.out.println("lock2 acquired.");
            } else {
                System.out.println("cannot acquire lock2, releasing lock1.");
                lock1.unlock();
                continue;
            }

            System.out.println("executing first operation.");
            break;
        }
        lock2.unlock();
        lock1.unlock();
    }

    public void operation2() {
        while (true) {
            tryLock(lock2, 50);
            System.out.println("lock2 acquired, trying to acquire lock1.");
            sleep(50);
            if (lock1.tryLock()) {
                System.out.println("lock1 acquired.");
            } else {
                System.out.println("cannot acquire lock1, releasing lock2.");
                lock2.unlock();
                continue;
            }

            System.out.println("executing second operation.");
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }

    private boolean tryLock(Lock lock, int ms) {
        try {
            return lock.tryLock(ms, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Never happens");
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Never happens");
        }
    }
}
