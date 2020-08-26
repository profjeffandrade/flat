package ifes.flat.rl;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.writeCollTo;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ifes.flat.Fsa;

/**
 * Codifica um autômato finito determinístico (AFD). Classe concreta que
 * implementa a interface `Dfa`. A classe é parametrizada (genérica) com o tipo
 * que representa os estados do autômato e o tipo que representa os símbolos do
 * alfabeto.
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> tipo dos objetos que representam estados do autômato
 * @param <A> tipo dos objetos que representam símbolos do alfabeto
 */
public class Dfa<S, A> implements Fsa<S, A, S, A> {

    private final Set<A> alphabet;
    private final Set<S> states;
    private final Map<Pair<S, A>, S> transFn;
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
    public Dfa(
            Set<A> alphabet,
            Set<S> states,
            Map<Pair<S, A>, S> transFn,
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

    /**
     * Função de transição do AFD. Basicamente é um <em>wrapper</em> em volta do
     * dicionário `transFn` para tornar mais conveniente o uso da função de
     * transição.
     *
     * @param si estado de origem da transição
     * @param x símbolo da transição
     * @return estado de destino da transição
     */
    @Override
    public S trans(S si, A x) {
        S sj = transFn.get(p(si, x));
        return sj;
    }

    /**
     * Função de transição estendida para AFD. A função de transição estendida
     * representa informalmente “partindo do estado `si` em que estado o
     * autômato terminará depois de processar a cadeia `w`”. Como visto no
     * material de apoio, a função de transição estendida para AFD pode ser
     * implementada por uma função recursiva simples à partir da função de
     * transição. A cadeia `w` é representada por uma lista de símbolos do
     * alfabeto.
     *
     * @param si estado de origem da transição extendida
     * @param w cadeia da transição estendida
     * @return estado de destino da transição estendida
     */
    @Override
    public S extTrans(S si, List<A> w) {
        // TODO: Implemente a função de transição estendida para AFD
        throw new UnsupportedOperationException("Função ainda não implementada.");
    }

    @Override
    public boolean accept(List<A> w) {
        S sj = extTrans(initState, w);
        return finalStates.contains(sj);
    }

    @Override
    public String toString() {
        return "DfaConcrete{" + "alphabet=" + alphabet + ", states=" + states
                + ", transFn=" + transFn + ", initState=" + initState
                + ", finalStates=" + finalStates + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.alphabet);
        hash = 67 * hash + Objects.hashCode(this.states);
        hash = 67 * hash + Objects.hashCode(this.transFn);
        hash = 67 * hash + Objects.hashCode(this.initState);
        hash = 67 * hash + Objects.hashCode(this.finalStates);
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
        final Dfa<?, ?> other = (Dfa<?, ?>) obj;
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
        writer.println("dfa");
        writeCollTo(alphabet, writer);
        writer.println();
        writeCollTo(states, writer);
        writer.println();
        writer.println(initState);
        writeCollTo(finalStates, writer);
        writer.println();
        transFn.forEach((trigger, sj) -> {
            writer.printf("%s %s %s\n", trigger._1, trigger._2, sj);
        });
    }

}
