import java.util.Timer;
import java.util.TimerTask;

public class CloseTimer {
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new Tarefa(), 5000);
    }

    static class Tarefa extends TimerTask {
        public void run() {
            TimerTask task = new TimerTask() {
                public void run() {
                    System.exit(0);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 0);
        }
    }
}
