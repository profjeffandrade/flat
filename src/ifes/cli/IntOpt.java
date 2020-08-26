package ifes.cli;

/**
 *
 * @author jefferson
 */
public class IntOpt<C extends Config> extends Opt<C, Integer> {
    
    public IntOpt(String shortName, String longName, String valueName) {
        super("int", shortName, longName, valueName, "", 
                false, true, (String x) -> Integer.parseInt(x));
    }
        
}
