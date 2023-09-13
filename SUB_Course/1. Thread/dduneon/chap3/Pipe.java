package chap3;

public class Pipe {
    private int data;

    private boolean empty = true;

    public synchronized int receive() {
        // 파이프가 비워져 있는 동안 기다린다.
        while (empty) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // 파이프가 채워지면 일정시간 기다린다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 파이프의 데이터는 반환하고, 다시 받을 수 있도록 한다.
        empty = true;
        return data;
    }

    public synchronized void send(int data) {
        // 파이프가 채워져 있는 동안 기다린다.
        while (!empty) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 파이프가 비워지면 일정시간 기다린다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 파이프에 데이터를 채우고 가져갈 수 있도록 한다.
        this.data = data;
        empty = false;
    }
}
