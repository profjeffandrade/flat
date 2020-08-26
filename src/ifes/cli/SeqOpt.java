package ifes.cli;

import ifes.data.Seq;
import java.util.Arrays;

/**
 *
 * @author jefferson
 */
public class SeqOpt<C extends Config> extends Opt<C, Seq<String>> {

    public String separator;

    public SeqOpt(String shortName, String longName, String valueName) {
        super("seq", shortName, longName, valueName, "", false, true);
        this.separator = ",";
        this.convert = (String x) -> {
            String[] xs = x.split(this.separator);
            return Seq.from(xs);
        };
    }

    @Override
    public boolean isSequence() {
        return true;
    }

}
