public class SequentialRelaxer {

    private RelaxableArray relaxableArray;
    private boolean precisionReached;

    public boolean isPrecisionReached() {
        return precisionReached;
    }

    public void setPrecisionReached(boolean precisionReached) {
        this.precisionReached = precisionReached;
    }

    public SequentialRelaxer(RelaxableArray relaxableArray) {
        this.relaxableArray = relaxableArray;
    }

    private double average(double[][] arrayToRelax, int x, int y) {
        return (arrayToRelax[x][y-1] + arrayToRelax[x][y+1] + arrayToRelax[x-1][y] + arrayToRelax[x+1][y])/4;
    }

    private boolean relaxArray(int[][] arrayToRelax) {
        boolean arrayRelaxed = false;

        return arrayRelaxed;
    }
}
