package chap3;

import java.util.concurrent.ThreadLocalRandom;

public class Sender implements Runnable {
    final Pipe pipe;

    public Sender(Pipe pipe) {
        this.pipe = pipe;
    }

    public void run() {
        // pipe를 통해서 데이터를 전송한다.
        this.pipe.send(4);

        // 전송에 성공하면 일정시간 기다린다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
