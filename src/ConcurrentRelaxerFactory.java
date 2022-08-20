import java.util.*;

public class ConcurrentRelaxerFactory implements RelaxerFactory{

    private RelaxationContext context;
    private RelaxableArray relaxableArray;

    public ConcurrentRelaxerFactory(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.context = context;
    }

    public Relaxer createRelaxer() {
        return new ConcurrentRelaxer();
    }

//    public Relaxer createRelaxer() {
//        Map<Integer, int[]> rowsMap = rowAssignment();
//        return new ConcurrentRelaxerRunnable(relaxableArray, context, rowsMap);
//    }

    private Map<Integer, int[]> rowAssignment() {
        Map<Integer, int[]> rowsMap = new HashMap<>();
        int totalRowsToAssign = context.getArraySize() - 2;
        int noOfThreads = context.getNoOfThreads();
        /*
        For 5v5, totalRowsToAssign is 3. Skip the first (0) and last (4) rows as they are boundaries. 
        Range should be 1,2,3.
         */
        int rowsPerThread = totalRowsToAssign / noOfThreads;
        int remaining = totalRowsToAssign % noOfThreads;
        // TODO: Come up with a formula so that every thread can come up with their own start and end row numbers. Makes this method obsolete.
        for (int i = 0; i < noOfThreads - remaining; i++) {
            int[] rows = new int[rowsPerThread];
            for (int j = 0; j < rowsPerThread; j++) {

            }
        }
        for (int i = 1; i <= context.getNoOfThreads(); i++) {

        }
        return rowsMap;
    }
}
