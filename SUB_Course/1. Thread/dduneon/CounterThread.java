public class CounterThread extends Thread {
    String name;
    int count;

    public CounterThread(String name) {
        this.name = name;
        this.count = 1;
    }

    @Override
    public void run() {
        Thread.currentThread().interrupt();
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(this.name + " : " + this.count++);
        }
    }
}
