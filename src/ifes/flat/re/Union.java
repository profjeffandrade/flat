package ifes.flat.re;

import ifes.data.Pair;
import ifes.Utils;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jefferson
 */
public class Union<A> extends Regex<A> {

    private final Regex<A> re1;
    private final Regex<A> re2;

    public Union(Regex<A> re1, Regex<A> re2) {
        this.re1 = re1;
        this.re2 = re2;
    }

    /**
     * Retorna o conjunto de todos os <em>matches</em> reconhecidos pela ER
     * `re1` ou pela ER `re2`.
     *
     * @param w a cadeia de entrada
     * @return o conjunto de todos os <em>matches</em> reconhecidos por `re1` ou
     * por `re2`
     */
    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        Set<Pair<List<A>, List<A>>> s1 = re1.matches(w);
        Set<Pair<List<A>, List<A>>> s2 = re2.matches(w);
        return Utils.union(s1, s2);
    }

    @Override
    public String toString() {
        return "Union{" + "re1=" + re1 + ", re2=" + re2 + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.re1);
        hash = 37 * hash + Objects.hashCode(this.re2);
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
        final Union<?> other = (Union<?>) obj;
        if (!Objects.equals(this.re1, other.re1)) {
            return false;
        }
        if (!Objects.equals(this.re2, other.re2)) {
            return false;
        }
        return true;
    }

}
