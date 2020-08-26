package ifes.cli;

/**
 *
 * @author jefferson
 */
public class BoolOpt<C extends Config> extends Opt<C, Boolean> {
    
    public BoolOpt(String shortName, String longName, String valueName) {
        super("bool", shortName, longName, valueName, "", 
                false, true, (String x) -> Boolean.parseBoolean(x));
    }
        
}
