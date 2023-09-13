package chap3;

import java.time.LocalDateTime;

public class TestExam4 {
    public static void main(String[] args) {
        Pipe pipe = new Pipe();
        Thread sender = new Thread(new Sender(pipe));
        Thread receiver = new Thread(new Receiver(pipe, 9));

        System.out.println("[ " + LocalDateTime.now() + " ] Sender : " + sender.getState()
                + ", Receiver : " + receiver.getState());

        sender.start();
        receiver.start();

        while (!Thread.interrupted()) {
            try {
                if ((sender.getState() == Thread.State.TERMINATED)
                        && (receiver.getState() == Thread.State.TERMINATED)) {
                    break;
                }
                Thread.sleep(1000);;
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("[ " + LocalDateTime.now() + " ] Sender : " + sender.getState()
                + ", Receiver : " + receiver.getState());
    }

}
