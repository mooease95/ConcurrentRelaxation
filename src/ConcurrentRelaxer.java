public class ConcurrentRelaxer implements Runnable{

    @Override
    public void run() {
        try {
            System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty.");
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }
}
