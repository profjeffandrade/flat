package ifes.cli;

/**
 *
 * @author jefferson
 */
public class FloatOpt<C extends Config> extends Opt<C, Float> {
    
    public FloatOpt(String shortName, String longName, String valueName) {
        super("float", shortName, longName, valueName, "", 
                false, true, (String x) -> Float.parseFloat(x));
    }
        
}
