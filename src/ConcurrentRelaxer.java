public class ConcurrentRelaxer implements Runnable {

    public static int count = 0;
    private boolean precisionReached;

    @Override
    public void run() {
        try {
            while (precisionReached) {
                count++;
                System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + count + ".");
            }
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }
}
