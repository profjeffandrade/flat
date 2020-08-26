package ifes.flat.rl;

import ifes.flat.rl.MooreMachine;
import ifes.flat.rl.FsaReader;
import ifes.flat.DataFormatException;
import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author jefferson
 */
public class MooreMachineReader extends FsaReader<String, String> {
    
    /**
     * Recebe a lista de linhas que definem o Autômato de Estados Finito (FSA) e
     * retorna o objeto que representa este autômato. A função `read` espera que
     * a <em>tab</em> que indica o tipo de Fsa tenha sido removida, i.e., a
 primeira linha da definição deve ser a definição do alfabeto.
     *
     * @return
     */
    @Override
    public Fsa<String, String, String, String> read(List<String> lines) throws DataFormatException {
        var alphabetIn = readAlphabet(lines);
        var alphabetOut = readAlphabetOut(alphabetIn._2);
        var states = readStates(alphabetIn._2);
        var init = readInitState(states._2);
        var finals = readFinalStates(init._2);
        var outFn = readOutputFunction(states._1.size(), finals._2);
        var transFn = readTransFunction(outFn._2);
        return new MooreMachine<String, String, String>(
                alphabetIn._1, 
                states._1, 
                transFn._1, 
                init._1, 
                finals._1, 
                alphabetOut._1,
                outFn._1
        );
    }
    
    @Override
    public Pair<Pair<String, String>, String> parseTransition(String line) throws DataFormatException {
        String[] tr = line.split(" ");
        if (tr.length != 3) {
            throw new DataFormatException(
                    String.format("Erro na definição da transição: %s", line));
        }
        var trigger = p(tr[0], tr[1]);
        var sj = tr[2];
        return p(trigger, sj);
    }
    
    private Pair<Set<String>, List<String>> readAlphabetOut(List<String> lines) {
        return readAlphabet(lines);
    }
    
    public Pair<Map<String, List<String>>, List<String>> readOutputFunction(int numStates, List<String> lines) throws DataFormatException {
        var outFn = new HashMap<String, List<String>>();
        for (int i = 0; i < numStates; i++) {
            String[] ts = lines.get(i).split(" ");
            var state = ts[0];
            var output = Arrays.asList(ts).subList(1, ts.length);
            outFn.put(state, output);
        }
        var rest = lines.subList(numStates, lines.size());
        return p(outFn, rest);
    }
    
}
