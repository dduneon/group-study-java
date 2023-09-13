package chap1;

public class Counter {
    String name;
    int count;

    public Counter(String name) {
        this.name = name;
        this.count = 1;
    }

    public void run() {
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
