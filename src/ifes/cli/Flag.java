package ifes.cli;

import java.util.Optional;

/**
 *
 * @author jefferson
 * @param <C>
 */
public class Flag<C extends Config> extends Opt<C, Object> {
    
    public Flag(String shortName, String longName) {
        super("flag", shortName, longName, "", "", false, true);
    }
    
    @Override
    public boolean isFlag() {
        return true;
    }

    @Override
    public boolean needValue() {
        return false;
    }

    @Override
    public String toString() {
        return "Flat{" 
                + "shortName=" + shortName 
                + ", longName=" + longName 
//                + ", description=" + description 
                + ", required=" + required 
                + ", visible=" + visible 
                + ", action=" + action 
                + '}';
    }

    @Override
    public boolean matchLongName(String a) {
        return a.equals(dashLongName());
    }
    
}
