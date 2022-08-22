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
        ConcurrentRelaxerRunnable runnable = new ConcurrentRelaxerRunnable(relaxableArray, context);
        runnable.createThreadsAndRun();
    }
}
