package ifes.flat.cfl;

import static ifes.Utils.forall;
import ifes.flat.Grammar;
import ifes.flat.Rule;
import java.util.List;
import java.util.Set;

/**
 * Representa gramáticas livres de contexto (context free grammars).
 *
 * @author jefferson
 */
public class Cfg<V, T> extends Grammar<V, T> {

    /**
     * Retorna verdadeiro se o conjunto de regras de produção é for livre de
     * contexto.
     *
     * @param rules o conjunto de regras de produção da gramática
     * @return verdadeiro se a gramática for livre de contexto, falso caso
     * contrário.
     */
    public static boolean isContextFree(List<Rule> rules) {
        boolean contextFree = forall(rules, r -> r.head.size() == 1);
        return contextFree;
    }

    /**
     * Construtor de gramática livre de contexto· Não existe diferença
     * extrutural entre uma gramática livre de contexto e uma gramática
     * irrestrita, entretanto as regras de produção da gramática livre de
     * contexto devem obedecer à restrição de ter a cabeça constituída por
     * apenas um símbolo não terminal. O contrutor checa essa restrição e se ela
     * falhar dispara uma exceção.
     *
     * @param nonTerminals conjunto de símbolos não terminais
     * @param terminals conjunto de símbolos terminais
     * @param rules conjunto de regras de produção
     * @param startSymbol símbolo inicial
     */
    public Cfg(Set<V> nonTerminals,
            Set<T> terminals,
            List<Rule> rules,
            V startSymbol) {
        super(nonTerminals, terminals, rules, startSymbol);
        if (!isContextFree(rules)) {
            throw new IllegalArgumentException("Grammar is not context free!");
        }
    }

    /**
     * Construtor auxiliar para criar uma gramática livre de contexto à partir
     * de uma outra gramática já existente. Se a gramática `g` não for uma CFG
     * será gerada uma exceção.
     *
     * @param g
     */
    public Cfg(Grammar<V, T> g) {
        this(g.nonTerminals, g.terminals, g.rules, g.startSymbol);
    }

    /**
     * Retorna uma nova gramática livre de contexto sem os símbolos inúteis. A
     * nova gramática é criada à partir desta, mas com os símbolos inúteis
     * removidos de acordo com o algoritmo visto no livro texto (Paulo B.
     * Menezes, "Linguagens Formais e Autômatos." 6ª ed.).
     *
     * @return uma Cfg sem símbolos inúteis
     */
    public Cfg<V, T> removeUselessSymbols() {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    /**
     * Retorna uma nova gramática livre de contexto sem produções que geram
     * cadeias vazias. A nova gramática é criada à partir desta, mas com as
     * produções que geram a cadeia vazia removidas de acordo com o algoritmo
     * visto no livro texto (Paulo B. Menezes, "Linguagens Formais e Autômatos."
     * 6ª ed.).
     *
     * @return uma Cfg sem produções que gerem cadeia vazia
     */
    public Cfg<V, T> removeEmptyProductions() {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    /**
     * Retorna uma nova gramática livre de contexto sem produções fazem
     * substituição de variáveis. A nova gramática é criada à partir desta, mas
     * com as produções que geram apenas substituição de variáveis removidas de
     * acordo com o algoritmo visto no livro texto (Paulo B. Menezes,
     * "Linguagens Formais e Autômatos." 6ª ed.).
     *
     * @return uma Cfg sem produções se apenas substituam variáveis
     */
    public Cfg<V, T> removeSubstituteVariables() {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    /**
     * Retorna uma nova gramática livre de contexto simplificada. A nova
     * gramática é criada à partir desta aplicando as simplificações definidas
     * nos métodos `removeUselessSymbols`, `removeEmptyProductions` e
     * `removeSubstituteVariables` na ordem apropriada, conforme explicado no
     * livro texto (Paulo B. Menezes, "Linguagens Formais e Autômatos." 6ª ed.).
     *
     * @return uma Cfg simplificada
     */
    public Cfg<V, T> simplify() {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

}
