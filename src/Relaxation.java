public class Relaxation {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.err.println("Too few arguments!!");
            System.exit(1);
        }
        Solver solver = new Solver(args);
        solver.start();
    }
}
