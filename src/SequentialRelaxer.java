public class SequentialRelaxer {

    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;
    private double targetPrecision;
    private int stepsTaken;
    private boolean debug;

    public SequentialRelaxer(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        precisionReached = false;
        this.targetPrecision = context.getPrecision();
        stepsTaken = 0;
        this.debug = context.isDebug();
    }

    private double averageArray(double[][] array, int x, int y) {
        return (array[x][y-1] + array[x][y+1] + array[x-1][y] + array[x+1][y])/4;
    }

    public void relaxArray() {
        System.out.println("****************");
        System.out.println("Starting to relax array sequentially");
        int size = arrayToRelax.length;
        boolean needsAnotherIteration = true;
        while (needsAnotherIteration) {
            needsAnotherIteration = false;
            stepsTaken++;
            double[][] newArrayToRelax = new double[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(arrayToRelax[i], 0, newArrayToRelax[i], 0, arrayToRelax[0].length);
            }
            for (int row = 1; row < size-1; row++) {
                for (int column = 1; column < size-1; column++) {
                    double newAvgValue = averageArray(newArrayToRelax, row, column);
                    arrayToRelax[row][column] = newAvgValue;
                    boolean precisionReachedForCurrentValue = checkPrecision(newAvgValue, row, column);
                    if (!precisionReachedForCurrentValue) {
                        needsAnotherIteration = true; // if we haven't reached precision, we need another iteration
                    }
                }
            }
        }
        System.out.println("****************");
        System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
        if (debug) ProgramHelper.logArray(relaxableArray, arrayToRelax);
    }

    private boolean checkPrecision(double relaxedValue, int row, int column) {
        double correctValue = relaxableArray.getValueInCorrectArray(row, column);
        return Math.abs(relaxedValue - correctValue) <= targetPrecision;
    }
}
