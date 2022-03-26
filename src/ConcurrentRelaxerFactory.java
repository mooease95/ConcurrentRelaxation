public class ConcurrentRelaxerFactory {

    private RelaxationContext context;

    public ConcurrentRelaxerFactory(RelaxableArray relaxableArray, RelaxationContext context) {
        this.context = context;
        create(relaxableArray);
    }

    public void create(RelaxableArray relaxableArray) {
        ConcurrentRelaxer concurrentRelaxer = new ConcurrentRelaxer(relaxableArray, context);
        createThreads(concurrentRelaxer);
    }

    private void createThreads(ConcurrentRelaxer concurrentRelaxer) {
        int noOfThreads = context.getNoOfThreads();
        if (noOfThreads > context.getArraySize()-2) {
            noOfThreads = context.getArraySize()-2;
            System.out.println("Warning: Number of threads specified is more than required for optimum relaxation. " +
                    "Flooring to " + noOfThreads + ".");
        }
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(concurrentRelaxer);
            t.start();
        }
        ConcurrentRelaxer.count++;
        System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + ConcurrentRelaxer.count +
                ".");
    }
}
