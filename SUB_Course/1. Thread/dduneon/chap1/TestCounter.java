package chap1;

public class TestCounter {
    public static void main(String[] args) {
        Counter counter = new Counter("Counter");
        CounterThread counterThread = new CounterThread("Counter 1");
        counterThread.start();
        counter.run();

        /**
         * if counter.run() execute before counterThread.run() ?
         */
    }
}
