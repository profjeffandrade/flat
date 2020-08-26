package ifes.flat.re;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.append;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author jefferson
 * @param <A>
 */
public class Concat<A> extends Regex<A> {

    public final Regex<A> re1;
    public final Regex<A> re2;

    public Concat(Regex<A> re1, Regex<A> re2) {
        this.re1 = re1;
        this.re2 = re2;
    }

    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        return re1.matches(w).stream()
                .flatMap(m1 -> {
                    return re2.matches(m1._2).stream()
                            .map(m2 -> p(append(m1._1, m2._1), m2._2));
                })
                .collect(Collectors.toSet());
    }

    @Override
        public String toString() {
        return "Concat{" + "re1=" + re1 + ", re2=" + re2 + '}';
    }

    @Override
        public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.re1);
        hash = 11 * hash + Objects.hashCode(this.re2);
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
        final Concat<?> other = (Concat<?>) obj;
        if (!Objects.equals(this.re1, other.re1)) {
            return false;
        }
        if (!Objects.equals(this.re2, other.re2)) {
            return false;
        }
        return true;
    }
    
}
