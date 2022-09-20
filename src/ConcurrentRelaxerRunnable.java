import java.util.concurrent.CyclicBarrier;

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

    private CyclicBarrier startRelaxation;
    private CyclicBarrier precisionCheckpoint;

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

    public double[][] setupAndRunThreads() {
        startRelaxation = new CyclicBarrier(noOfThreads, new IncrementCounter());
        precisionCheckpoint = new CyclicBarrier(noOfThreads, new FinishRelaxation());
        for (int i = 0; i < noOfThreads; i++) {
            Thread t = new Thread(this);
            t.start();
        }
        System.out.println("Apparently have finished relaxing!!!");
        return arrayToRelax;
    }

    @Override
    public void run() {
        int[] rowList = rowAllocator.allocateRows();
        try {
            for (int i = 0; i < rowList.length; i++) {
                RelaxerUtils.printThreadDebugMessages("Reporting for duty. Row picked= " + rowList[i], true);
            }
            initiateRelaxation(rowList);
        } catch (Exception e) {
            System.err.println("Exception = " + e);
        }
        RelaxerUtils.printThreadDebugMessages("Have finished relaxing", true);
    }

    private void initiateRelaxation(int[] rowList) {
        int size = arrayToRelax.length;
        boolean needsAnotherIteration = true;
        // while (needsAnotherIteration) {
        for (int tmp = 0; tmp < 2; tmp++) {
            needsAnotherIteration = false;
            double[][] newArrayToRelax = new double[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(arrayToRelax[i], 0, newArrayToRelax[i], 0, arrayToRelax[0].length);
            }
            try {
                startRelaxation.await();
            } catch (Exception e) {
                RelaxerUtils.printThreadDebugMessages("Exception during relaxation starting barrier: \n" + e, false);
                throw new RuntimeException(e);
            }
            // RelaxerUtils.printThreadDebugMessages("First row= " + rowList[0] + ", last row= " + rowList[rowList.length - 1], true);
            for (int row = rowList[rowList.length - 1]; row <= rowList[0]; row++) {
                // RelaxerUtils.printThreadDebugMessages("Row= " + row, true);
                for (int column = 1; column < size - 1; column++) {
                    // RelaxerUtils.printThreadDebugMessages("Relaxing [" + row + "][" + column + "]", true);
                    double newAvgValue = RelaxerUtils.averageArray(newArrayToRelax, row, column);
                    arrayToRelax[row][column] = newAvgValue;
                    boolean precisionReachedForCurrentValue = RelaxerUtils.checkPrecision(relaxableArray, newAvgValue, row, column, targetPrecision);
                    if (!precisionReachedForCurrentValue) {
                        needsAnotherIteration = true;
                    }
                }
            }
            try {
                precisionCheckpoint.await();
            } catch (Exception e) {
                RelaxerUtils.printThreadDebugMessages("Exception at precision check barrier: \n" + e, false);
                throw new RuntimeException(e);
            }
        }
    }

    class IncrementCounter implements Runnable {
        @Override
        public void run() {
            stepsTaken++;
            RelaxerUtils.printThreadDebugMessages("Final thread to hit startRelaxation barrier. Current step= " + stepsTaken, true);
        }
    }

    class FinishRelaxation implements Runnable {
        @Override
        public void run() {
            System.out.println("****************");
            System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
//            if (debug) ProgramHelper.logArray(relaxableArray, arrayToRelax);
        }
    }
}
