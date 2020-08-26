package ifes.cli;

import java.util.Optional;

/**
 *
 * @author jefferson
 * @param <C>
 */
public class Cmd<C extends Config> extends Opt<C, Object> {
    
    public Cmd(String name) {
        super("cmd", name, "", "", "", false, true);
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public boolean needValue() {
        return false;
    }

    @Override
    public String toString() {
        return "Cmd{" 
                + "name =" + shortName 
//                + ", description=" + description 
                + ", required=" + required 
                + ", visible=" + visible 
                + ", action=" + action 
                + '}';
    }

    @Override
    public boolean matchShortName(String a) {
        return shortName.equals(a);
    }

    @Override
    public boolean matchLongName(String a) {
        return false;
    }

    @Override
    public Optional<String> getLongNameValue(String a) {
        return Optional.empty();
    }

}
