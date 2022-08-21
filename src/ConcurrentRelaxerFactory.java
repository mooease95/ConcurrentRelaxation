import java.util.*;

public class ConcurrentRelaxerFactory implements RelaxerFactory{

    private RelaxationContext context;
    private RelaxableArray relaxableArray;

    public ConcurrentRelaxerFactory(RelaxableArray relaxableArray, RelaxationContext context) {
        this.relaxableArray = relaxableArray;
        this.context = context;
    }

    public Relaxer createRelaxer() {
        return new ConcurrentRelaxer(relaxableArray, context);
    }
}
