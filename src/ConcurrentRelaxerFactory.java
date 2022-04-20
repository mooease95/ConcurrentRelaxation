import java.util.Stack;

public class ConcurrentRelaxerFactory {

    private RelaxationContext context;
    private RelaxableArray relaxableArray;

    public ConcurrentRelaxerFactory(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.context = context;
    }

    public ConcurrentRelaxer create() {
        Stack<Integer> rows = rowAssignment();
        return new ConcurrentRelaxer(relaxableArray, context, rows);
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
