package ifes.flat.rl;

import ifes.flat.DataFormatException;
import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author jefferson
 */
public abstract class FsaReader<S1, A1> {

    public abstract Fsa<String, String, S1, A1> read(List<String> lines) throws DataFormatException;

    public Pair<Set<String>, List<String>> readAlphabet(List<String> lines) {
        assert lines.size() > 0;
        var symbols = lines.get(0).split(" ");
        var alphabet = new HashSet<String>(Arrays.asList(symbols));
        var rest = lines.subList(1, lines.size());
        return p(alphabet, rest);
    }

    public Pair<Set<String>, List<String>> readStates(List<String> lines) {
        assert lines.size() > 0;
        String[] qs = lines.get(0).split(" ");
        var states = new HashSet<String>(Arrays.asList(qs));
        var rest = lines.subList(1, lines.size());
        return p(states, rest);
    }

    public Pair<String, List<String>> readInitState(List<String> lines) {
        assert lines.size() > 0 && lines.get(0).trim().length() < 1;
        var init = lines.get(0).trim();
        var rest = lines.subList(1, lines.size());
        return p(init, rest);
    }

    public Pair<Set<String>, List<String>> readFinalStates(List<String> lines) {
        assert lines.size() > 0;
        var fs = lines.get(0).split(" ");
        var finalStates = new HashSet<String>();
        if (!fs[0].trim().equals("!!")) {
            finalStates.addAll(Arrays.asList(fs));
        }
        var rest = lines.subList(1, lines.size());
        return p(finalStates, rest);
    }

    public Pair<Map<Pair<String, A1>, S1>, List<String>> readTransFunction(List<String> lines) throws DataFormatException {
        var trans = new HashMap<Pair<String, A1>, S1>();
        int i = 0;
        while (endOfTransitions(i, lines)) {
            var tr = parseTransition(lines.get(i));
            trans.put(tr._1, tr._2);
            i += 1;
        }
        var rest = lines.subList(i, lines.size());
        return p(trans, rest);
    }
    
    public boolean endOfTransitions(int i, List<String> lines) {
        return i < lines.size();
    }
    
    public abstract Pair<Pair<String, A1>, S1> parseTransition(String line) throws DataFormatException;
    
}
