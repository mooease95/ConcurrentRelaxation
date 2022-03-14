public class Relaxation {

    private static int noOfThreads;
    private static double precision;
    private static int arraySize;
    private static int randMin;
    private static int randMax;
    private static boolean debug;

    public static void main(String[] args) {
        if (args.length < 6) {
            System.err.println("Too few arguments!!");
            System.exit(1);
        }

        noOfThreads = Integer.parseInt(args[0]);
        precision = Double.parseDouble(args[1]);
        arraySize = Integer.parseInt(args[2]);
        randMin = Integer.parseInt(args[3]);
        randMax = Integer.parseInt(args[4]);
        debug = Boolean.parseBoolean(args[5]);

        ProgramHelper.logArgs(noOfThreads, precision, arraySize, randMin, randMax);

        Relaxation relaxation = new Relaxation();
        relaxation.solveSequentially();
        relaxation.solveConcurrently();

    }

    private void solveConcurrently() {
        System.out.println("Starting ThreadManager!");
        ThreadManager threadManager = new ThreadManager(noOfThreads);
        threadManager.createThreads();
    }

    private void solveSequentially() {
        RelaxableArray relaxableArray = new RelaxableArray(arraySize, randMin, randMax);
        if (debug) ProgramHelper.logArray(relaxableArray, null);
        long start = System.currentTimeMillis();
        SequentialRelaxer sequentialRelaxer = new SequentialRelaxer(relaxableArray, precision, debug);
        sequentialRelaxer.relaxArray();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time taken to sequentially relax=[" + timeElapsed/1000 + "s].");

    }
}
