public class PrintScreenThread extends Thread {
    @Override
    public void run() {
        PrintScreenListener listener = new PrintScreenListener();
        listener.start();
    }
}