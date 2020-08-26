package ifes.flat.re;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jefferson
 */
public class Literal<A> extends Regex<A> {

    public final A symbol;

    public Literal(A symbol) {
        this.symbol = symbol;
    }

    /**
     * Retorna um <em>match</em> caso o primeiro símbolo da cadeia de entrada
     * seja igual ao símbolo da expressão regular, e nenhum match caso
     * contrário. Caso retorne um <em>match</em>, o primeiro elemento do par
     * será uma lista com exatamente um elemento (o símbolo da ER), e o segundo
     * elemento será a lista com o restante dos símbolos da cadeira de entrada.
     *
     * @param w a cadeia de entrada
     * @return Um conjunto com exatamente um <em>match</em> caso o primeiro
     * símbolo da cadeia seja igual ao símbolo da expressão, e um conjunto vazio
     * caso contrário
     */
    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        if (!w.isEmpty() && symbol.equals(w.get(0))) {
            var m = p(List.of(w.get(0)), w.subList(1, w.size()));
            return Set.of(m);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.symbol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Literal<?> other = (Literal<?>) obj;
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        return true;
    }

}
