package ifes.flat.re;

import ifes.data.Pair;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author jefferson
 * @param <A> tipo de dado que representa os símbolos do alfabeto reconhecido
 * pela ER
 */
public abstract class Regex<A> implements Cloneable {

    public static <A> Regex<A> nothing() {
        return new Nothing<>();
    }
    
    public static <A> Regex<A> empty() {
        return new Empty<>();
    }
    
    public static Regex<Character> compile(String pattern) {
        var p = new RegexParser(pattern);
        return p.parse();
    }
    
    public Regex<A> concat(Regex<A> that) {
        return new Concat(this, that);
    }

    public Regex<A> union(Regex<A> that) {
        return new Union(this, that);
    }

    public Regex<A> star() {
        return new Star(this);
    }

    /**
     * Gera o conjunto com todas as correpondências entre a expressão regular e
     * a cadeia de entrada. O valor retornado e o conjunto contendo pares
     * ordenados que representam as correspondências entre a ER e a cadeia de
     * entada. Cada par ordenado tem como primeiro componente a parte da cadeia
     * de entrada que foi reconhecida pela ER, e como segundo componente o
     * restante da cadeia de entrada, i.e., o que sobrou depois de removida a
     * parte que foi reconhecida. De modo geral, se um par ordenado do conjunto
     * de resposta é `(w1,w2)`, temos que ter `w1++w2 = w` (onde `++` está sendo
     * considerado como concatenação das cadeias).
     *
     * @param w cadeia de entrada
     * @return o conjunto contendo todos as correspondências possíveis entre
     * esta expressão regular e a cadeia de entrada
     */
    public abstract Set<Pair<List<A>, List<A>>> matches(List<A> w);

    /**
     * Determina se esta expressão regular aceita a cadeia de entrada. Uma
     * expressão regular aceita a cadeia de entrada se a ER “consome” toda a
     * cadeia de entrada.
     *
     * @param w cadia de entrada
     * @return Verdadeiro se a ER aceita a cadeia de entrada, e falso caso
     * contrário
     */
    public boolean accepts(List<A> w) {
        Set<Pair<List<A>, List<A>>> ms = matches(w);
        Set<Pair<List<A>, List<A>>> mss = ms.stream()
                .filter(m -> m._2.isEmpty())
                .collect(Collectors.toSet());
        return mss.isEmpty();
    }
}
