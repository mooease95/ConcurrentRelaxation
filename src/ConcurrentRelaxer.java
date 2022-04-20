import java.util.Stack;

public class ConcurrentRelaxer implements Runnable {

    private int count = 0;
    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;

    private int noOfThreads;
    private double targetPrecision;
    private int arraySize;
    private int stepsTaken;
    private boolean debug;
    
    private Stack<Integer> rows;

    // private synchronized int[] rowsAssigned = {};

    public ConcurrentRelaxer(RelaxableArray relaxableArray, RelaxationContext context, Stack<Integer> rows) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        this.precisionReached = false;
        this.rows = rows;

        this.noOfThreads = context.getNoOfThreads();
        this.targetPrecision = context.getPrecision();
        this.arraySize = context.getArraySize();
        this.debug = context.isDebug();
    }

    public void createThreadsAndRun() {
        if (noOfThreads > arraySize-2) {
            System.out.println("Warning: Number of threads specified is more than required for optimum relaxation. " +
                    "Flooring to " + noOfThreads + ".");
            noOfThreads = arraySize-2;
        }
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        int rowNumber = -1;
        try {
            while (!rows.empty()) {
                synchronized (this) {
                    if (!rows.empty()) {
                        rowNumber = rows.pop();
                        // TODO: This should populate an array/ArrayList for each thread.
                    }
                }
            }
//            if (debug) System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + count + ".");
            System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Row picked=" + rowNumber + ".");
            while (precisionReached) {
                count++; // TODO: This should do the relaxation
            }
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }
}
