package chap2;

public class CounterRunnable implements Runnable {
    Thread thread;
    String name;
    int count;

    public CounterRunnable(String name) {
        this.thread = new Thread(this);
        this.name = name;
        count = 1;
    }

    public void start() {
        this.thread.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println(name + " : " + count++);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
