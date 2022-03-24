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
        int noOfThreads = context.getNoOfThreads();
        if (noOfThreads > context.getArraySize()-2) {
            noOfThreads = context.getArraySize()-2;
            System.out.println("Warning: Number of threads specified is more than required for optimum relaxation. " +
                    "Flooring to " + noOfThreads + ".");
        }
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(this);
            t.start();
        }
        System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + count + ".");
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
