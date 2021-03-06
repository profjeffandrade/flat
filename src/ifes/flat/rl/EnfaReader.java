package ifes.flat.rl;

import ifes.flat.DataFormatException;
import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author jefferson
 */
public class EnfaReader extends FsaReader<Set<String>, Optional<String>> {

    /**
     * Recebe a lista de linhas que definem o Autômato de Estados Finito (FSA) e
     * retorna o objeto que representa este autômato. A função `read` espera que
     * a <em>tab</em> que indica o tipo de Fsa tenha sido removida, i.e., a
 primeira linha da definição deve ser a definição do alfabeto.
     *
     * @return
     */
    @Override
    public Fsa<String, String, Set<String>, Optional<String>> read(List<String> lines) throws DataFormatException {
        var alphabet = readAlphabet(lines);
        var states = readStates(alphabet._2);
        var init = readInitState(states._2);
        var finals = readFinalStates(init._2);
        var trans = readTransFunction(finals._2);
        return new Enfa(alphabet._1, states._1, trans._1, init._1, finals._1);
    }

    @Override
    public Pair<Pair<String, Optional<String>>, Set<String>> parseTransition(String line) throws DataFormatException {
        String[] tr = line.split(" ");
        if (tr.length != 3) {
            throw new DataFormatException(
                    String.format("Erro na definição da transição: %s", line));
        }
        Pair<String, Optional<String>> trigger;
        if (tr[1].trim().equals("_")) {
            trigger = p(tr[0], Optional.empty());
        } else {
            trigger = p(tr[0], Optional.of(tr[1]));
        }
        List<String> lsj = Arrays.asList(tr).subList(2, tr.length);
        Set<String> ssj = new HashSet(lsj);
        return p(trigger, ssj);
    }

}
