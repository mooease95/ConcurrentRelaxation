public class ConcurrentRelaxer implements Runnable{

    private int count = 0;

    @Override
    public void run() {
        try {
            count++;
            System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + count + ".");
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }
}
