package ifes.flat;

import static ifes.Utils.makeString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe que representa regras de produção em uma gramática. Uma regra de
 * produção em uma gramática irrestrita é composta por uma cabeça (parte à
 * esquerda da seta) e por um corpo (parte à direita da seta). Tanto a cabeça da
 * regra, quanto o corpo, são sequências de símbolos do conjunto de terminair ou
 * do conjunto de nao terminais, i.e., são elementos da união deste dois
 * conjuntos. Como os tipos dos elementos não terminais e dos elementos
 * terminais são diferentes, a cabeça e o corpo são representados por listas do
 * tipo `Either` que armazena um entre dois tipos de valores. A cabeça da regra
 * de produção não pode ser vazia.
 *
 * @author jefferson
 */
public class Rule {
    
    public static Rule rule(List head, List body) {
        Rule r = new Rule(head, body);
        return r;
    }
    
    public static List<Rule> r(String s) {
        List<Rule> rs = new ArrayList<>();
        String[] parts = s.split("\u2192|->");
        
        String[] h = parts[0].split(" ");
        List<String> head = Arrays.asList(h).stream()
                .filter(x -> !x.isBlank() && !x.equals("\u03F5") && !x.equals("&epsilon;"))
//                .map(x -> x.equals("&lt;") ? "<" : x)
//                .map(x -> x.equals("&gt;") ? ">" : x)
                .collect(Collectors.toList());
        
        String[] bs = parts[1].split("\\|");
        for (String b: bs) {
           String[] b1 = b.split(" ");
           List<String> body = Arrays.asList(b1).stream()
                   .filter(x -> !x.isBlank() && !x.equals("\u03F5") && !x.equals("&epsilon;"))
//                   .map(x -> x.equals("&lt;") ? "<" : x)
//                   .map(x -> x.equals("&gt;") ? ">" : x)
                   .collect(Collectors.toList());
           
           Rule r1 = new Rule(head, body);
           rs.add(r1);
        }
        return rs;
    }

    public final List head;
    public final List body;

    public Rule(List head, List body) {
        if (head.isEmpty()) {
            throw new IllegalArgumentException("Empty head in production rule.");
        }
        this.head = Collections.unmodifiableList(head);
        this.body = Collections.unmodifiableList(body);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.head);
        hash = 89 * hash + Objects.hashCode(this.body);
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
        final Rule other = (Rule) obj;
        if (!Objects.equals(this.head, other.head)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String r = show();
        return "Rule{" + r + '}';
    }

    public String show() {
        String hd = makeString(head, " ", "", "");
        String bd = makeString(body, " ", "", "");
        bd = bd.isBlank() ? "\u03F5" : bd;
        return hd + " \u2192 " + bd;
    }

}
