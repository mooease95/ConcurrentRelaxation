public class SequentialRelaxer {

    private RelaxableArray relaxableArray;
    private double[][] arrayToRelax;
    private boolean precisionReached;
    private double targetPrecision;
    private int stepsTaken;

    public SequentialRelaxer(RelaxableArray relaxableArray, double targetPrecision) {
        this.relaxableArray = relaxableArray;
        this.arrayToRelax = relaxableArray.getArrayToRelax();
        precisionReached = false;
        this.targetPrecision = targetPrecision;
        stepsTaken = 0;
    }

    private double average(int x, int y) {
        return (arrayToRelax[x][y-1] + arrayToRelax[x][y+1] + arrayToRelax[x-1][y] + arrayToRelax[x+1][y])/4;
    }

    // TODO: New implementation of relaxing. Problems facing/solved: (1) Last value in array reaches precision so we stop.
    // TODO: (2) One value reaches precision and stops, so others stagnate and stop changing without reaching precision.
    public void relaxArrayTry01() {
        System.out.println("****************");
        System.out.println("Starting to relax array sequentially");
        int size = arrayToRelax.length;
        boolean needAnotherIteration = true;
        while (needAnotherIteration) {
            needAnotherIteration = false;
            stepsTaken++;
            for (int row = 1; row < size-1; row++) {
                for (int column = 1; column < size - 1; column++) {
                    double value = average(row, column);
                    arrayToRelax[row][column] = value;
                    boolean precisionReachedForCurrentValue = checkPrecision(value, row, column);
                    if (!precisionReachedForCurrentValue) {
                        needAnotherIteration = true;
                    }
                }
            }
        }
        System.out.println("****************");
        System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
        ProgramHelper.logArray(relaxableArray, arrayToRelax);
    }

    public void relaxArrayTry00() {
        System.out.println("****************");
        System.out.println("Starting to relax array sequentially");
        int size = arrayToRelax.length;
        while (!precisionReached) {
            precisionReached = true;
            stepsTaken++;
            for (int row = 1; row < size-1; row++) {
                for (int column = 1; column < size-1; column++) {
                    double value = arrayToRelax[row][column];
                    System.out.println("Looking into row=[" + row + "], column=[" + column + "], value=[" + value + "].");
                    boolean isPrecisionReachedForValue = checkPrecision(value, row, column);
                    if (!isPrecisionReachedForValue && !precisionReached) {
                        precisionReached = false;
                        System.out.println("Precision not reached. Averaging.");
                        arrayToRelax[row][column] = average(row, column);
                        System.out.println("Value after average = [" + arrayToRelax[row][column] + "].");
                    }
                }
            }
        }
        System.out.println("****************");
        System.out.println("PRECISION REACHED FOR ALL!! Steps taken=[" + stepsTaken + "].");
        ProgramHelper.logArray(relaxableArray, arrayToRelax);
    }

    private boolean checkPrecision(double relaxedValue, int row, int column) {
        double correctValue = relaxableArray.getValueInCorrectArray(row, column);
        System.out.println("correctValue=[" + correctValue + "], relaxedValue=[" + relaxedValue + "].");
        if (Math.abs(relaxedValue-correctValue) <= targetPrecision) {
            return true;
        } else {
            return false;
        }
    }
}
