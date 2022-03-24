public class RelaxationContext {

    private int noOfThreads;
    private double precision;
    private int arraySize;
    private int randMin;
    private int randMax;
    private boolean debug;

    public RelaxationContext(int noOfThreads,
                             double precision,
                             int arraySize,
                             int randMin,
                             int randMax,
                             boolean debug) {
        this.noOfThreads = noOfThreads;
        this.precision = precision;
        this.arraySize = arraySize;
        this.randMin = randMin;
        this.randMax = randMax;
        this.debug = debug;
    }

    public int getNoOfThreads() {
        return noOfThreads;
    }

    public double getPrecision() {
        return precision;
    }

    public int getArraySize() {
        return arraySize;
    }

    public int getRandMin() {
        return randMin;
    }

    public int getRandMax() {
        return randMax;
    }

    public boolean isDebug() {
        return debug;
    }
}
