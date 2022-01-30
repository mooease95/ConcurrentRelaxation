public class ThreadManager {

    private int noOfThreads;

    public ThreadManager(int noOfThreads) {
        this.noOfThreads = noOfThreads;
    }

    public void createThreads() {
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(new ConcurrentRelaxer());
            t.start();
        }
    }
}
