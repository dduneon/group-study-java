package chap2;

public class TestCounter {
    public static void main(String[] args) {

        // (1) Run CounterRunnable with Thread Instance
        // CounterRunnable counter = new CounterRunnable("CounterRunnable");
        // CounterRunnable counter2 = new CounterRunnable("CounterRunnable");
        // Thread thread = new Thread(counter);
        // Thread thread2 = new Thread(counter2);
        // thread.start();
        // thread2.start();

        // (2) Run CounterRunnable with Thread class variable
        CounterRunnable counterRunnable = new CounterRunnable("CounterRunnable");
        CounterRunnable counterRunnable2 = new CounterRunnable("CounterRunnable2");

        counterRunnable.start();
        counterRunnable2.start();
    }
}
