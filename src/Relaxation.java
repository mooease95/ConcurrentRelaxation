public class Relaxation {

    public static void main(String[] args) {
        if (args.length < 5) {
            System.err.println("Too few arguments!!");
            System.exit(1);
        }

        int noOfThreads = Integer.parseInt(args[0]);
        double precision = Double.parseDouble(args[1]);
        int arraySize = Integer.parseInt(args[2]);
        int randMin = Integer.parseInt(args[3]);
        int randMax = Integer.parseInt(args[4]);

        ProgramHelper.logArgs(noOfThreads, precision, arraySize, randMin, randMax);

        RelaxableArray relaxableArray = new RelaxableArray(arraySize, randMin, randMax);
        // ProgramHelper.logArray(relaxableArray, null);

        long start = System.currentTimeMillis();
        SequentialRelaxer sequentialRelaxer = new SequentialRelaxer(relaxableArray, precision);
        sequentialRelaxer.relaxArray();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time taken to sequentially relax=[" + timeElapsed/1000 + "s].");

    }
}
