package ifes.flat.rl;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.writeCollTo;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import ifes.flat.Fsa;

/**
 * Codifica um autômato finito não determinístico (AFN).Classe concreta que
 implementa a interface `Nfa`.A classe é parametrizada (genérica) com o tipo
 que representa os estados do autômato e o tipo que representa os símbolos do
 alfabeto.
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> Tipo que dos objetos que representam os estados do autômato
 * @param <A> Tipo dos objetos que representam os símbolos do alfabeto
 */
public class Enfa<S,A> implements Fsa<S, A, Set<S>, Optional<A>> {

    private final Set<A> alphabet;
    private final Set<S> states;
    private final Map<Pair<S, Optional<A>>, Set<S>> transFn;
    private final S initState;
    private final Set<S> finalStates;

    /**
     * Cria um objeto que representa um AFD.
     *
     * @param alphabet conjunto de símbolos do alfabeto do autômato
     * @param states conjunto de estados do autômato
     * @param transFn dicionário que codifica a função de transição do autômato
     * @param initState estado inicial do autômato
     * @param finalStates conjunto de estados finais do autômato
     */
    public Enfa(
            Set<A> alphabet,
            Set<S> states,
            Map<Pair<S, Optional<A>>, Set<S>> transFn,
            S initState,
            Set<S> finalStates) {
        this.alphabet = Collections.unmodifiableSet(alphabet);
        this.states = Collections.unmodifiableSet(states);
        this.transFn = Collections.unmodifiableMap(transFn);
        this.initState = initState;
        this.finalStates = Collections.unmodifiableSet(finalStates);
    }

    @Override
    public Set<A> getAlphabet() {
        return alphabet;
    }

    @Override
    public Set<S> getStates() {
        return states;
    }

    @Override
    public Set<S> getFinalStates() {
        return finalStates;
    }

    @Override
    public Set<S> trans(S si, Optional<A> x) {
        Pair<S,Optional<A>> key = p(si, x);
        Set<S> setSj = transFn.get(key);
        return (setSj != null) ? setSj : Collections.emptySet();
    }

    /**
     * Função de transição estendida para AFN. A função de transição estendida
     * representa informalmente “partindo do conjunto de estados `si` em que 
     * conjunto de estados o autômato terminará após processar a cadeia `w`”. 
     * Como visto no material de apoio, a função de transição estendida para 
     * AFN pode ser implementada por uma função recursiva relativamente simples 
     * à partir da função de transição. A cadeia `w` é representada por uma 
     * lista de símbolos do alfabeto.
     *
     * @param si conjunto de estados de origem da transição extendida
     * @param w cadeia da transição estendida
     * @return conjunto de estados de destino da transição estendida
     */
    @Override
    public Set<S> extTrans(Set<S> si, List<A> w) {
        // TODO: Implemente a função de transição estendida para AFD
        throw new UnsupportedOperationException("Função ainda não implementada.");
    }

    /**
     * Função de aceitação do autômato. Dada uma cadeia de caracteres `w`, 
     * determina se o autômato aceita ou não a cadeia.
     * 
     * @param w cadeia que será testada se é aceita ou não pelo autômato
     * @return `true` se a cadeia é aceita pelo autômato e `false` caso 
     * contrário.
     */
    @Override
    public boolean accept(List<A> w) {
        // TODO: Implemente a função de transição estendida para AFD
        throw new UnsupportedOperationException("Função ainda não implementada.");
    }

    @Override
    public String toString() {
        return "Enfa{" 
                + "alphabet=" + alphabet 
                + ", states=" + states 
                + ", transFn=" + transFn 
                + ", initState=" + initState 
                + ", finalStates=" + finalStates 
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.alphabet);
        hash = 13 * hash + Objects.hashCode(this.states);
        hash = 13 * hash + Objects.hashCode(this.transFn);
        hash = 13 * hash + Objects.hashCode(this.initState);
        hash = 13 * hash + Objects.hashCode(this.finalStates);
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
        final Enfa<?, ?> other = (Enfa<?, ?>) obj;
        if (!Objects.equals(this.alphabet, other.alphabet)) {
            return false;
        }
        if (!Objects.equals(this.states, other.states)) {
            return false;
        }
        if (!Objects.equals(this.transFn, other.transFn)) {
            return false;
        }
        if (!Objects.equals(this.initState, other.initState)) {
            return false;
        }
        if (!Objects.equals(this.finalStates, other.finalStates)) {
            return false;
        }
        return true;
    }

    @Override
    public void writeTo(PrintWriter writer) {
        writer.println("nfae");
        writeCollTo(alphabet, writer);
        writer.println();
        writeCollTo(states, writer);
        writer.println();
        writer.println(initState);
        writeCollTo(finalStates, writer);
        writer.println();
        transFn.forEach((trigger, ssj) -> {
            writer.printf("%s ", trigger._1);
            writer.printf("%s ", trigger._2.isEmpty() ? "_" : trigger._2.get());
            writeCollTo(ssj, writer);
            writer.println();
        });
    }
    
}
