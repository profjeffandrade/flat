package ifes;

import ifes.data.BooleanBuilder;
import ifes.data.Counter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author jefferson
 */
public class Utils {

    public static <T> String makeString(List<T> lst) {
        return makeString(lst, ", ", "[", "]", x -> String.valueOf(x));
    }
    
    public static <T> String makeString(List<T> lst, Function<T, String> format) {
        return makeString(lst, ", ", "[", "]", format);
    }
    
    public static <T> String makeString(List<T> lst, String sep) {
        return makeString(lst, sep, "[", "]", x -> String.valueOf(x));
    }
    
    public static <T> String makeString(List<T> lst, String sep, Function<T, String> format) {
        return makeString(lst, sep, "", "", format);
    }
    
    public static <T> String makeString(List<T> lst, String sep, String left, String right) {
        return makeString(lst, sep, left, right, x -> String.valueOf(x));
    }
    
    public static <T> String makeString(List<T> lst, String sep, String left, String right, Function<T, String> format) {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        var count = new Counter();
        lst.forEach(x -> {
            if (count.get() > 0) sb.append(sep);
            String s = format.apply(x);
            sb.append(s);
            count.inc();
        });
        sb.append(right);
        return sb.toString();
    }
    
        public static <A> List<A> append(List<A>... ls) {
        ArrayList<A> acc = new ArrayList<>();
        for (List<A> l: ls) {
            acc.addAll(l);
        }
        return Collections.unmodifiableList(acc);
    }
    
    public static <A> Set<A> union(Set<A>... xs) {
        HashSet<A> acc = new HashSet<>();
        for (Set<A> x: xs) {
            acc.addAll(x);
        }
        return Collections.unmodifiableSet(acc);
    }
    
    public static List<Character> strToList(String text) {
        var cs = text.toCharArray();
        var lc = new ArrayList<Character>();
        for (Character c: cs) {
            lc.add(c);
        }
        return lc;
    }
    
    public static <A> void writeCollTo(Collection<A> coll, PrintWriter writer) {
        writeCollTo(coll, writer, " ");
    }
    
    public static <A> void writeCollTo(Collection<A> coll, PrintWriter writer, String delim) {
        int i = 0;
        for (A x: coll) {
            if (i > 0) writer.print(delim);
            writer.print(x);
            i += 1;
        }
    }
        
    public static <T> boolean forall(Collection<T> coll, Predicate<T> p) {
        BooleanBuilder result = new BooleanBuilder(true);
        coll.forEach(x -> {
            result.and(p.test(x));
        });
        return result.get();
    }

    public static <T> boolean exists(Collection<T> coll, Predicate<T> p) {
        BooleanBuilder result = new BooleanBuilder(false);
        coll.forEach(x -> {
            result.or(p.test(x));
        });
        return result.get();
    }

    /**
     * Lê todo o texto informado em `reader` e retorna como uma lista de linhas.
     *
     * @return o texto informado na entrada do programa
     * @throws IOException
     */
    public static List<String> readLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String t = reader.readLine().trim();
        while (t != null) {
            lines.add(t);
            t = reader.readLine();
        }
        return lines;
    }

}
