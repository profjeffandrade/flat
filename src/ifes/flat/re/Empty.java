package ifes.flat.re;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jefferson
 * @param <A>
 */
public class Empty<A> extends Regex<A> {

    public Empty() {
        super();
    }

    /**
     * Retorna um <em>match</em> que não consome nada da cadeia de entrada.
     *
     * @param w cadeia de entrada
     * @return Um conjunto contendo um único <em>match</em> onde o primeiro
     * elemento do par ordenado é a lista vazia, e o segundo elemento é a cadeia
     */
    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        return Set.of(p(Collections.emptyList(), w));
    }

    @Override
    public String toString() {
        return "\u0190";
    }

}
