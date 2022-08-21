import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ConcurrentRelaxer implements Relaxer {

    private RelaxableArray relaxableArray;
    private RelaxationContext context;

    public ConcurrentRelaxer(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.context = context;
    }

    @Override
    public void relaxArray() {
        Stack<Integer> rowsMap = rowAssignment();
        ConcurrentRelaxerRunnable runnable = new ConcurrentRelaxerRunnable(relaxableArray, context, rowsMap);
        runnable.createThreadsAndRun();
    }

    private Stack<Integer> rowAssignment() {
        int totalRowsToAssign = relaxableArray.getArraySize() - 2;
        Stack<Integer> rows = new Stack<>();
        /*
        For 5v5, totalRowsToAssign is 3. Skip the first (0) and last (4) rows as they are boundaries.
        Range should be 1,2,3.
         */
        for (int i = 1; i <= totalRowsToAssign; i++) {
            rows.push(i);
        }
        return rows;
    }
}
