package ifes.cli;

/**
 *
 * @author jefferson
 */
public class CharOpt<C extends Config> extends Opt<C, Character> {
    
    public CharOpt(String shortName, String longName, String valueName) {
        super("char", shortName, longName, valueName, "", 
                false, true, (String x) -> x.charAt(0));
    }
        
}
