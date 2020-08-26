package ifes.flat;

import static ifes.flat.Rule.r;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Um parser simples para gramáticas. Este parser retorna uma gramática
 * irrestrita onde o tipo dos símbolos terminais e dos não terminais é string. O
 * formato de definição da gramática é o seguinte:
 * <ul>
 * <li>Símbolos não terminais são delimitados por parênteses angulares
 * (`<` e `>`)</li>
 * <li>Para usar o símbolo de menor na gramática, deve-se escrever `&lt;`</li>
 * <li>Para usar o símbolo de maior na gramática, deve-se escrever `&gt;`</li>
 * <li>Todo símbolo que não for reconhecido como não terminal será tratado como
 * terminal.</li>
 * <li>Todos os símbolos devem ser separados por espaço.</li>
 * <li>Não é admitido espaço no nome dos símbolos.</li>
 * <li>A cabeça das regras são separadas do corpo por uma seta, i.e., `->` ou
 * `→` ('\u2192').<li>
 * <li>É possível definir várias regras com a mesma cabeça separando os
 * diferentes corpos por barra vertical (`|`).</li>
 * <li>Não pode have quebra de linhas na definição das regras, mesmo quando
 * houver mais de um corpo.</li>
 * <li>Regras com corpo vazio podem ser definidas usando o símbolo epsilon, que
 * é representado por `&epsilon;` ou por `ϵ` ('\u03F5').</li>
 * <li>A cabeça da primeira regra será considerada o símbolo inicial da
 * gramática.</li>
 * </ul>
 *
 * Exemplo de uma gramática aceita pelo parser:
 *
 * ```
 * <expr> -> <parcela> | <expr> + <parcela> | <expr> - <parcela>
 * <parcela> -> <fator> | <parcela> * <fator> | <parcela> / <fator>
 * <fator> -> <sinal> <fator> | <numero> | <variavel> | ( <expr> )
 * <sinal> -> - | &epsilon;
 * <numero> -> <digito> | <numero> <digito>
 * <variavel> -> <letra> | <restovar>
 * <letra> -> <lower> | <upper>
 * <restovar> -> <letra> | <digito>
 * <digito> -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
 * <lower> -> a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z
 * <upper> -> A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z
 * ```
 *
 * @author jefferson
 */
public class SimpleGrammarParser {

    public static Grammar<String, String> parseGrammar(String text) {
        String[] lines = text.split("\\r?\\n");
        return parseGrammar(Arrays.asList(lines));
    }

    public static Grammar<String, String> parseGrammar(List<String> lines) {
        List<Rule> rules = new ArrayList<>();
        lines.forEach(l -> {
            rules.addAll(r(l));
        });

        Set<String> nonTerminals = new HashSet<>();
        Set<String> terminals = new HashSet<>();
        for (Rule r : rules) {
            collectSymbols(r.head, nonTerminals, terminals);
            collectSymbols(r.body, nonTerminals, terminals);
        }

        String start = (String) rules.get(0).head.get(0);

        Grammar<String, String> g
                = new Grammar(nonTerminals, terminals, List.copyOf(rules), start);
        return g;
    }

    private static void collectSymbols(List lst, Set<String> nonTerminals, Set<String> terminals) {
        Stream<String> symbols = lst.stream().map(x -> (String) x);
        symbols.forEach(s -> {
            if (isNonTerminal(s)) {
                nonTerminals.add(s);
            } else {
                terminals.add(s);
            }
        });
    }

    private static boolean isNonTerminal(String s) {
        return s.startsWith("<") && s.endsWith(">");
    }

}
