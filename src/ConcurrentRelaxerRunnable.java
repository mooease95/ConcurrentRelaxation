public class ConcurrentRelaxerRunnable implements Runnable {

    private int count = 0;
    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;

    private int noOfThreads;
    private double targetPrecision;
    private int arraySize;
    private int stepsTaken;
    private boolean debug;

    private RowAllocator rowAllocator;

    public ConcurrentRelaxerRunnable(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        this.precisionReached = false;

        this.noOfThreads = context.getNoOfThreads();
        this.targetPrecision = context.getPrecision();
        this.arraySize = context.getArraySize();
        this.debug = context.isDebug();

        createRowAllocator();
    }

    private void createRowAllocator() {
        if (noOfThreads > arraySize - 2) {
            noOfThreads = arraySize - 2;
            System.out.println("Warning: Number of threads specified is more than required for optimum relaxation. " +
                    "Flooring to " + noOfThreads + ".");
        }
        rowAllocator = new RowAllocator(noOfThreads, arraySize - 2);
    }

    public void createThreadsAndRun() {
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        int[] rowList = rowAllocator.allocateRows();
        try {
            if (debug) System.out.println("[" + Thread.currentThread().getName() + "]: Reporting for duty. Count=" + count + ".");
            for (int i = 0; i < rowList.length; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "]: Reporting for duty. Row picked= " + rowList[i] + ".");
            }
            while (precisionReached) {
                count++; // TODO: This should do the relaxation
            }
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }
}
