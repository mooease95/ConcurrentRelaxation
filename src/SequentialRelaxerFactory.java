public class SequentialRelaxerFactory implements RelaxerFactory{
    @Override
    public Relaxer createRelaxer(RelaxableArray relaxableArray, RelaxationContext context) {
        return new SequentialRelaxer(relaxableArray, context);
    }
}
