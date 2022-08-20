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
        RelaxableArray relaxableArray = new RelaxableArray(arraySize, randMin, randMax);
        solveSequentially(relaxableArray);
        // solveConcurrently(relaxableArray);
    }

    private void solveSequentially(RelaxableArray relaxableArray) {
        if (debug) ProgramHelper.logArray(relaxableArray, null);
        long start = System.nanoTime();
        RelaxerFactory seqeuntialRelaxerFactory = new SequentialRelaxerFactory(relaxableArray, context);
        Relaxer sequentialRelaxer = seqeuntialRelaxerFactory.createRelaxer();
        sequentialRelaxer.relaxArray();
        long finish = System.nanoTime();
        float timeElapsed = finish - start;
        System.out.println("Time taken to sequentially relax=[" + timeElapsed/1000000 + " milliseconds].");
    }

    private void solveConcurrently(RelaxableArray relaxableArray) {
        if (debug) ProgramHelper.logArray(relaxableArray, null);
        System.out.println("****************");
        System.out.println("Starting to relax array concurrently");
        System.out.println("****************");
        ConcurrentRelaxerFactory concurrentRelaxerFactory = new ConcurrentRelaxerFactory(relaxableArray, context);
        Relaxer concurrentRelaxer = concurrentRelaxerFactory.createRelaxer();
        // concurrentRelaxer.createThreadsAndRun();
    }
}
