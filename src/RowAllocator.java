import java.util.Stack;

public class RowAllocator {

    private final int threads;
    private final int assignableRows;
    Stack<Integer> rowsStack;
    private boolean remainderAssigned = false;

    public RowAllocator(int threads, int assignableRows) {
        this.threads = threads;
        this.assignableRows = assignableRows;

        rowsStack = new Stack<>();
        rowAssignment();
        if (assignableRows % threads == 0) {
            remainderAssigned = true;
        }
    }

    int[] allocateRows() {
        synchronized (this) {
            int rowsPerThread = assignableRows / threads;
            int[] rowsForThread;
            if (!remainderAssigned) {
                rowsPerThread = rowsPerThread + 1;
                remainderAssigned = true;
            }
            rowsForThread = new int[rowsPerThread];
            for (int i = 0; i < rowsPerThread; i++) {
                int rowFromStack = rowsStack.pop();
                rowsForThread[i] = rowFromStack;
            }
            return rowsForThread;
        }
    }

    private void rowAssignment() {
        /*
        For 5v5, totalRowsToAssign is 3. Skip the first (0) and last (4) rows as they are boundaries.
        Range should be 1,2,3.
         */
        for (int i = 1; i <= assignableRows; i++) {
            rowsStack.push(i);
        }
    }


}
