public class RelaxerUtils {

    public static double averageArray(double[][] array, int x, int y) {
        return (array[x][y-1] + array[x][y+1] + array[x-1][y] + array[x+1][y])/4;
    }

    public static boolean checkPrecision(RelaxableArray relaxableArray, double relaxedValue, int row, int column, double targetPrecision) {
        double correctValue = relaxableArray.getValueInCorrectArray(row, column);
        double obtainedPrecision = Math.abs(relaxedValue - correctValue);
        printThreadDebugMessages(String.format("Checking precision. Correct value=[%s], relaxed value=[%s], targetPrecision=[%s], obtainedPrecision=[%s]",
                                                correctValue, relaxedValue, targetPrecision, obtainedPrecision), true);
        return obtainedPrecision <= targetPrecision;
    }

    public static void printThreadDebugMessages(String message, boolean info) {
        if (info) {
            System.out.println("[" + Thread.currentThread().getName() +"]: " + message + ".");
        } else {
            System.err.println("[" + Thread.currentThread().getName() +"]: " + message + ".");
        }
    }
}
