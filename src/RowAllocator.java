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
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "]: Arrived to get rows allocated."); // TODO: For debug purpose, remove.
            int rowsPerThread = assignableRows / threads;
            int[] rowsForThread;
            if (!remainderAssigned) {
                System.out.println("[" + threadName + "]: Remainder not assigned."); // TODO: For debug purpose, remove.
                rowsPerThread = rowsPerThread + 1;
                remainderAssigned = true;
            }
            rowsForThread = new int[rowsPerThread];
            System.out.println("[" + threadName + "]: Will take number of rows= " + rowsForThread.length); // TODO: For debug purpose, remove.
            for (int i = 0; i < rowsPerThread; i++) {
                int rowFromStack = rowsStack.pop();
                System.out.println("[" + threadName + "]: Taking row from stack: " + rowFromStack); // TODO: For debug purpose, remove.
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
            System.out.println("Adding to stack: " + i); // TODO: For debug purpose, remove.
            rowsStack.push(i);
        }
    }


}
