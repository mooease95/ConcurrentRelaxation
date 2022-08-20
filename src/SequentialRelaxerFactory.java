public class SequentialRelaxerFactory implements RelaxerFactory{

    private RelaxableArray relaxableArray;
    private RelaxationContext context;

    public SequentialRelaxerFactory(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.context = context;
    }
    @Override
    public Relaxer createRelaxer() {
        return new SequentialRelaxer(relaxableArray, context);
    }
}
