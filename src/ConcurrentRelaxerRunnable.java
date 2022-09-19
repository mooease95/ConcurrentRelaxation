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
            for (int i = 0; i < rowList.length; i++) {
                RelaxerUtils.printThreadDebugMessages("Reporting for duty. Row picked= " + rowList[i]);
            }
            initiateRelaxation(rowList);
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
    }

    private void initiateRelaxation(int[] rowList) {
        // All threads are doing this constantly. But each thread should have its own
        int size = arrayToRelax.length;
        boolean needsAnotherIteration = true;
        while (needsAnotherIteration) {
            needsAnotherIteration = false;
            stepsTaken++; // TODO: This is now being modified by all the threads, should be only one.
            double[][] newArrayToRelax = new double[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(arrayToRelax[i], 0, newArrayToRelax[i], 0, arrayToRelax[0].length);
            }
            // TODO: All threads need to pause here before they can go round starting to modify arrayToRelax;
            RelaxerUtils.printThreadDebugMessages("First row= " + rowList[0] + ", last row= " + rowList[rowList.length - 1]);
            for (int row = rowList[rowList.length - 1]; row <= rowList[0]; row++) {
                RelaxerUtils.printThreadDebugMessages("Row= " + row);
                for (int column = 1; column < size - 1; column++) {
                    RelaxerUtils.printThreadDebugMessages("Relaxing [" + row + "][" + column + "]");
                    double newAvgValue = RelaxerUtils.averageArray(newArrayToRelax, row, column);
                    arrayToRelax[row][column] = newAvgValue; // TODO: This can't happen until other threads have finished copying.
                    boolean precisionReachedForCurrentValue = RelaxerUtils.checkPrecision(relaxableArray, newAvgValue, row, column, targetPrecision);
                    if (!precisionReachedForCurrentValue) {
                        needsAnotherIteration = true;
                    }
                }
            }
            // TODO: All threads should pause here for other threads
            // TODO: But this is the run() method! Every thread is running a different version of it!
        }
//        System.out.println("****************");
//        System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
//        if (debug) ProgramHelper.logArray(relaxableArray, arrayToRelax);
    }
}
