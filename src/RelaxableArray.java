import java.util.concurrent.ThreadLocalRandom;

public class RelaxableArray {

    private boolean precisionReached;
    private int arraySize;
    private double[][] arrayToRelax;

    private double[][] correctRelaxedArray;

    public RelaxableArray(int arraySize, int randMin, int randMax) {
        this.arraySize = arraySize;
        arrayToRelax = new double[arraySize][arraySize];
        correctRelaxedArray = new double[arraySize][arraySize];
        generateArray(randMin, randMax);
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public boolean isPrecisionReached() {
        return precisionReached;
    }

    public void setPrecisionReached(boolean precisionReached) {
        this.precisionReached = precisionReached;
    }

    public double[][] getArrayToRelax() {
        return arrayToRelax;
    }

    public void setArrayToRelax(double[][] arrayToRelax) {
        this.arrayToRelax = arrayToRelax;
    }

    public double[][] getCorrectRelaxedArray() {
        return correctRelaxedArray;
    }

    private void generateArray(int randMin, int randMax) {
        for (int row = 0; row < arraySize; row++) {
            for (int column = 0; column < arraySize; column++) {
                double correctValue = ProgramHelper.functionInArray(row, column);
                correctRelaxedArray[row][column] = correctValue;
                arrayToRelax[row][column] = correctValue;
                if (row != 0 && row != arraySize-1 && column != 0 && column != arraySize-1) {
                    arrayToRelax[row][column] = ThreadLocalRandom.current().nextInt(randMin, randMax);
                }
            }
        }
    }

    private double getValueInCorrectArray(int row, int column) {
        return correctRelaxedArray[row][column];
    }
}
