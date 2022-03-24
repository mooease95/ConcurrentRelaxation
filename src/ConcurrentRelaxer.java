public class ConcurrentRelaxer implements Runnable{

    private int count = 0;
    private RelaxationContext context;

    public ConcurrentRelaxer(RelaxationContext context) {
        this.context = context;
    }

    public void start() {
        createThreads();
    }

    private void createThreads() {
        for (int i = 0; i < context.getNoOfThreads(); i++) {
            Thread t = new Thread(this);
            t.start();
        }
    }

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
