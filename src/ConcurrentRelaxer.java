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
        double[][] arrayToRelax = runnable.setupAndRunThreads();
    }
}
