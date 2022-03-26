public class Solver {

    private int noOfThreads;
    private double precision;
    private int arraySize;
    private int randMin;
    private int randMax;
    private boolean debug;
    private RelaxationContext context;

    public Solver(String[] args) {
        noOfThreads = Integer.parseInt(args[0]);
        precision = Double.parseDouble(args[1]);
        arraySize = Integer.parseInt(args[2]);
        randMin = Integer.parseInt(args[3]);
        randMax = Integer.parseInt(args[4]);
        debug = Boolean.parseBoolean(args[5]);

        ProgramHelper.logArgs(noOfThreads, precision, arraySize, randMin, randMax);

        context = new RelaxationContext(noOfThreads, precision, arraySize, randMin, randMax, debug);
    }

    public void start() {
        solveSequentially();
        solveConcurrently();
    }

    private void solveConcurrently() {
        RelaxableArray relaxableArray = new RelaxableArray(arraySize, randMin, randMax); // TODO: Array should be generated before solvers are called.
        if (debug) ProgramHelper.logArray(relaxableArray, null);
        System.out.println("****************");
        System.out.println("Starting to relax array sequentially");
        System.out.println("****************");
        ConcurrentRelaxerFactory concurrentRelaxerFactory = new ConcurrentRelaxerFactory(relaxableArray, context);
    }

    private void solveSequentially() {
        RelaxableArray relaxableArray = new RelaxableArray(arraySize, randMin, randMax); // TODO: Array should be generated before solvers are called.
        if (debug) ProgramHelper.logArray(relaxableArray, null);
        long start = System.currentTimeMillis();
        SequentialRelaxer sequentialRelaxer = new SequentialRelaxer(relaxableArray, context);
        sequentialRelaxer.relaxArray();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time taken to sequentially relax=[" + timeElapsed/1000 + "s].");

    }
}
