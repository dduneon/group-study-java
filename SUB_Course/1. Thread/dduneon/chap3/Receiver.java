package chap3;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class Receiver implements Runnable {
    final Pipe pipe;
    int endData;

    public Receiver(Pipe pipe, int endData) {
        this.pipe = pipe;
        this.endData = endData;
    }

    public void run() {
        // Pipe에서 데이터를 기다리며, 수신된 데이터가 endData이면 종료한다.
        int data = pipe.receive();
        if (data == endData)
            return;
        // 수신된 데이터가 endData가 아닌 경우, 임의의 시간을 기다린다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
