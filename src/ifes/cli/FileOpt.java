package ifes.cli;

import java.io.File;

/**
 *
 * @author jefferson
 */
public class FileOpt<C extends Config> extends Opt<C, File> {
    
    public FileOpt(String shortName, String longName, String valueName) {
        super("file", shortName, longName, valueName, "", 
                false, true, (String x) -> new File(x));
    }
    
}
