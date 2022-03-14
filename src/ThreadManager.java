public class ThreadManager {

    private int noOfThreads;

    public ThreadManager(int noOfThreads) {
        this.noOfThreads = noOfThreads;
    }

    public void createThreads() {
        ConcurrentRelaxer concurrentRelaxer = new ConcurrentRelaxer();
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(concurrentRelaxer);
            t.start();
        }
    }
}
