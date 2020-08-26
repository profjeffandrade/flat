package ifes.flat.rl;

import ifes.flat.rl.MealyMachine;
import ifes.flat.rl.FsaReader;
import ifes.flat.DataFormatException;
import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author jefferson
 */
public class MealyMachineReader extends FsaReader<Pair<String,List<String>>, String> {
    
    /**
     * Recebe a lista de linhas que definem o Autômato de Estados Finito (FSA) e
     * retorna o objeto que representa este autômato. A função `read` espera que
     * a <em>tab</em> que indica o tipo de Fsa tenha sido removida, i.e., a
 primeira linha da definição deve ser a definição do alfabeto.
     *
     * @return
     */
    @Override
    public Fsa<String, String, Pair<String,List<String>>, String> read(List<String> lines) throws DataFormatException {
        var alphabetIn = readAlphabet(lines);
        var alphabetOut = readAlphabetOut(alphabetIn._2);
        var states = readStates(alphabetIn._2);
        var init = readInitState(states._2);
        var finals = readFinalStates(init._2);
        var trans = readTransFunction(finals._2);        
        return new MealyMachine<String, String, String>(
                alphabetIn._1, 
                states._1, 
                trans._1, 
                init._1, 
                finals._1, 
                alphabetOut._1
        );
    }

    @Override
    public Pair<Pair<String, String>, Pair<String,List<String>>> parseTransition(String line) throws DataFormatException {
        String[] tr = line.split(" ");
        if (tr.length != 3) {
            throw new DataFormatException(
                    String.format("Erro na definição da transição: %s", line));
        }
        var trigger = p(tr[0], tr[1]);
        var sj = tr[2];
        var output = Arrays.asList(tr).subList(3, tr.length);
        var destiny = p(sj, output);
        return p(trigger, destiny);
    }

    private Pair<Set<String>, List<String>> readAlphabetOut(List<String> lines) {
        return readAlphabet(lines);
    }
    
}
