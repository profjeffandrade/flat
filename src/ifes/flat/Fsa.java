package ifes.flat;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> tipo dos objetos que representam estados do autômato
 * @param <A> tipo dos objetos que representam símbolos do alfabeto
 * @param <S1> tipo que representa o retorno da função de transição; será `S`
 * para AFD e `Set<S>` para AFN e AFNe
 * @param <A1> tipo que representa o <em>gatilho<em> da função de transição;
 * será `A` para AFD e AFN e `Optional<A>` para AFNe
 */
public interface Fsa<S, A, S1, A1> {

    public Set<A> getAlphabet();

    public Set<S> getStates();

    public Set<S> getFinalStates();

    public S1 trans(S si, A1 x);

    public S1 extTrans(S1 si, List<A> w);

    public boolean accept(List<A> w);

    public void writeTo(PrintWriter writer);
}
