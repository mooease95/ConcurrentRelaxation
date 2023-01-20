import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentRelaxerRunnable implements Runnable {

    private int count = 0;
    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;
    Map<String, Boolean> threadNeedsAnotherIteration;
    AtomicBoolean needsAnotherIteration;

    private int noOfThreads;
    private double targetPrecision;
    private int arraySize;
    private int stepsTaken;
    private boolean debug;

    private RowAllocator rowAllocator;

    private CyclicBarrier startRelaxation;
    private CyclicBarrier relaxationReset;
    private CyclicBarrier precisionCheckpoint;
    private CyclicBarrier precisionCompletion;

    public ConcurrentRelaxerRunnable(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        this.precisionReached = false;
        this.needsAnotherIteration = new AtomicBoolean(true);
        threadNeedsAnotherIteration = new HashMap<>();

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
        relaxationReset = new CyclicBarrier(noOfThreads, new ResetIterationValues());
        precisionCheckpoint = new CyclicBarrier(noOfThreads, new DecideContinuation());
        precisionCompletion = new CyclicBarrier(noOfThreads, new FinishRelaxation());
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
            for (int rows : rowList) {
                RelaxerUtils.printThreadDebugMessages("Reporting for duty. Row picked= " + rows, true);
            }
            initiateRelaxation(rowList);
        } catch (Exception e) {
            System.err.println("Exception by " + Thread.currentThread().getName() + "= " + e);
        }
        RelaxerUtils.printThreadDebugMessages("Have finished relaxing", true);
    }

    private void initiateRelaxation(int[] rowList) {
        RelaxerUtils.printThreadDebugMessages("Initiating relaxation", true);
        int size = arrayToRelax.length;
        boolean threadIterate = needsAnotherIteration.get(); // TODO: Another thread could have set this to false by the time one thread is looking at it!
        while (threadIterate) {
            RelaxerUtils.printThreadDebugMessages("Starting to iterate", true);
        // for (int tmp = 0; tmp < 2; tmp++) {
            // Every thread starts the iteration expecting it to be the last.
            threadNeedsAnotherIteration.put(Thread.currentThread().getName(), false);
            try {
                relaxationReset.await();
            } catch (Exception e) {
                RelaxerUtils.printThreadDebugMessages("Exception during relaxation resetting barrier: \n" + e, false);
            }

            RelaxerUtils.printThreadDebugMessages("Have reset all values", true);

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
            RelaxerUtils.printThreadDebugMessages("First row= " + rowList[0] + ", last row= " + rowList[rowList.length - 1], true);
            for (int row = rowList[rowList.length - 1]; row <= rowList[0]; row++) {
                RelaxerUtils.printThreadDebugMessages("Row= " + row, true);
                for (int column = 1; column < size - 1; column++) {
                    RelaxerUtils.printThreadDebugMessages("Relaxing [" + row + "][" + column + "]", true);
                    double newAvgValue = RelaxerUtils.averageArray(newArrayToRelax, row, column);
                    arrayToRelax[row][column] = newAvgValue;
                    boolean precisionReachedForCurrentValue = RelaxerUtils.checkPrecision(relaxableArray, newAvgValue, row, column, targetPrecision);
                    if (!precisionReachedForCurrentValue) {
                        // If a thread has not reached precision for any value, it resets its own expectation, expecting itself to iterate again.
                        this.threadNeedsAnotherIteration.put(Thread.currentThread().getName(), true);
                    }
                }
            }
            try {
                // End of the iteration, all threads wait. The last thread does a check to see if any other thread expects to do another iteration.
                precisionCheckpoint.await();
            } catch (Exception e) {
                RelaxerUtils.printThreadDebugMessages("Exception at precision check barrier: \n" + e, false);
                throw new RuntimeException(e);
            }
            // If all thread had reached precision and no one expects to iterate again, the global check remains false from the beginning of the iteration.
            threadIterate = needsAnotherIteration.get();
        }
        try {
            precisionCompletion.await();
        } catch (Exception e) {
            RelaxerUtils.printThreadDebugMessages("Exception at precision completion barrier: \n" + e, false);
            throw new RuntimeException(e);
        }
    }

    class IncrementCounter implements Runnable {
        @Override
        public void run() {
            stepsTaken++;
            RelaxerUtils.printThreadDebugMessages("Final thread to hit startRelaxation barrier. Current step= " + stepsTaken, true);
        }
    }

    class ResetIterationValues implements Runnable {
        @Override
        public void run() {
            needsAnotherIteration.compareAndSet(true, false);
        }
    }

    class DecideContinuation implements Runnable {
        @Override
        public void run() {
            if (threadNeedsAnotherIteration.containsValue(true)) {
                needsAnotherIteration.set(true);
            }
        }
    }

    class FinishRelaxation implements Runnable {
        @Override
        public void run() {
            System.out.println("****************");
            System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
            if (debug) ProgramHelper.logArray(relaxableArray, arrayToRelax);
        }
    }
}
