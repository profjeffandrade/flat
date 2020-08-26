package ifes.flat.re;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.append;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author jefferson
 */
public class Star<A> extends Regex<A> {

    private final Regex<A> re;

    public Star(Regex<A> re) {
        this.re = re;
    }

    @Override
    public Set<Pair<List<A>, List<A>>> matches(List<A> w) {
        var s1 = re.matches(w);
        if (s1.isEmpty()) {
            return Set.of(p(Collections.emptyList(), w));
        } else {
            return s1.stream()
                    .flatMap(m1 -> {
                        var st1 = Stream.of(m1);
                        var st2 = this.matches(m1._2).stream()
                                .map(m2 -> p(append(m1._1, m2._1), m2._2));
                        return Stream.concat(st1, st2);
                    })
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public String toString() {
        return "Star{" + "re=" + re + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.re);
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
        final Star<?> other = (Star<?>) obj;
        if (!Objects.equals(this.re, other.re)) {
            return false;
        }
        return true;
    }
}
