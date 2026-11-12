
import java.util.concurrent.CountDownLatch;

class Account {

    int balance;
    String name;

    Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
}

public class DeadlockDemo {

    public static void main(String[] args) {

        Account A = new Account("Account A", 1000);
        Account B = new Account("Account B", 1000);

        CountDownLatch ready = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {

            synchronized (A) {

                System.out.println("Thread 1 locked Account A");

                ready.countDown();

                try {
                    ready.await();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (B) {
                    System.out.println("Thread 1 transferred money");
                }
            }
        });

        Thread t2 = new Thread(() -> {

            synchronized (B) {

                System.out.println("Thread 2 locked Account B");

                ready.countDown();

                try {
                    ready.await();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (A) {
                    System.out.println("Thread 2 transferred money");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
