public class ConcurrentRelaxerFactory {

    private RelaxationContext context;

    public ConcurrentRelaxerFactory(RelaxationContext context) {
        this.context = context;
    }

    public void create() {
        createThreads();
    }

    private void createThreads() {
        int noOfThreads = context.getNoOfThreads();
        if (noOfThreads > context.getArraySize()-2) {
            noOfThreads = context.getArraySize()-2;
            System.out.println("Warning: Number of threads specified is more than required for optimum relaxation. " +
                    "Flooring to " + noOfThreads + ".");
        }
        ConcurrentRelaxer concurrentRelaxer = new ConcurrentRelaxer();
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(concurrentRelaxer);
            t.start();
        }
        ConcurrentRelaxer.count++;
        System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + ConcurrentRelaxer.count +
                ".");
    }
}
