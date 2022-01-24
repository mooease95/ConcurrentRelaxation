import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ProgramHelper {

    public static double functionInArray(double x, double y) {
        return x*x + y*y;
    }

    public static void logArgs(int noOfThreads, double precision, int arraySize, int randMin, int randMax) {
        System.out.println("Program arguments:");
        System.out.println("threads: " + noOfThreads + "\n"
                + "precision: " + precision + "\n"
                + "arraySize: " + arraySize + "\n"
                + "randMin: " + randMin + "\n"
                + "randMax: " + randMax);
    }

    public static void logArray(RelaxableArray relaxableArray, double[][] relaxedArray) {
        DecimalFormat df = new DecimalFormat("###.###");
        df.setRoundingMode(RoundingMode.CEILING);
        int arraySize = relaxableArray.getArraySize();
        System.out.println("Printing correct array.");
        double[][] array = relaxableArray.getCorrectRelaxedArray();
        for (int row = 0; row < arraySize; row++) {
            for (int column = 0; column < arraySize; column++) {
                System.out.print(df.format(array[row][column]));
                System.out.print("    ");
            }
            System.out.println("\n");
        }
        if (relaxedArray == null) {
            System.out.println("Printing array yet to be relaxed.");
            array = relaxableArray.getArrayToRelax();
        } else {
            System.out.println("Printing relaxed array.");
            array = relaxedArray;
        }
        for (int row = 0; row < arraySize; row++) {
            for (int column = 0; column < arraySize; column++) {
                System.out.print(df.format(array[row][column]));
                System.out.print("    ");
            }
            System.out.println("\n");
        }
    }
}
