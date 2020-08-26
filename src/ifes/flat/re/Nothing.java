package ifes.flat.re;

import ifes.data.Pair;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jefferson
 * @param <A>
 */
public class Nothing<A> extends Regex<A> {

    public Nothing() {
        super();
    }

    /**
     * Retorna um conjunto vazio, sinalizando que nenhum <em>match</em> foi
     * reconhecido. A expressão regular <em>nada</em>, por definição, nunca tem
     * sucesso em reconhecer caracteres.
     *
     * @param w cadeia de entrada
     * @return um conjunto vazio
     */
    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "\u2205";
    }
    
}
