public class TestCounter {
    public static void main(String[] args) {
        Counter counter = new Counter("Counter");
        CounterThread counterThread = new CounterThread("Counter 1");
        CounterThread counterThread2 = new CounterThread("Counter 2");
        counterThread.start();
        counter.run();
    }
}
