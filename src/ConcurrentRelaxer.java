public class ConcurrentRelaxer implements Runnable {

    public static int count = 0;
    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;

    private int noOfThreads;
    private double targetPrecision;
    private int stepsTaken;
    private boolean debug;

    // private synchronized int[] rowsAssigned = {};

    public ConcurrentRelaxer(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        this.precisionReached = false;

        this.noOfThreads = context.getNoOfThreads();
        this.targetPrecision = context.getPrecision();
        this.debug = context.isDebug();
    }

    @Override
    public void run() {
        try {
            while (precisionReached) {
                count++;
                if (debug) System.out.println("Thread=[" + Thread.currentThread().getName() + "] reporting for duty. Count=" + count + ".");
            }
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }

    private void rowAssignment() {
        int totalRowsToAssign = relaxableArray.getArraySize() - 2;
        if (totalRowsToAssign % noOfThreads == 0) {
            // TODO: Work here.
        }
    }
}
