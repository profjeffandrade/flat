package ifes.cli;

/**
 *
 * @author jefferson
 */
public class StrOpt<C extends Config> extends Opt<C, String> {
    
    public StrOpt(String shortName, String longName, String valueName) {
        super("str", shortName, longName, valueName, "", 
                false, true, (String x) -> x);
    }
    
}
