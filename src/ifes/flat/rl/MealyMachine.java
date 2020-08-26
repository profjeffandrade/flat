package ifes.flat.rl;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.append;
import static ifes.Utils.writeCollTo;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> tipo dos objetos que representam estados do autômato
 * @param <A> tipo dos objetos que representam símbolos do alfabeto de entrada
 * @param <B> tipo dos objetos que representam símbolos do alfabeto de saída
 */
public class MealyMachine<S, A, B> implements Fsa<S, A, Pair<S,List<B>>, A> {

    private final Set<A> alphabetIn;
    private final Set<S> states;
    private final Map<Pair<S, A>, Pair<S, List<B>>> transFn;
    private final S initState;
    private final Set<S> finalStates;
    private final Set<B> alphabetOut;
    
    public MealyMachine(
            Set<A> alphabetIn, 
            Set<S> states, 
            Map<Pair<S, A>, Pair<S, List<B>>> transFn, 
            S initState, 
            Set<S> finalStates,
            Set<B> alphabetOut) {
        this.alphabetIn = Collections.unmodifiableSet(alphabetIn);
        this.states = Collections.unmodifiableSet(states);
        this.transFn = Collections.unmodifiableMap(transFn);
        this.initState = initState;
        this.finalStates = Collections.unmodifiableSet(finalStates);
        this.alphabetOut = Collections.unmodifiableSet(alphabetOut);
    }

    @Override
    public Set<A> getAlphabet() {
        return alphabetIn;
    }

    public Set<B> getOutputAlphabet() {
        return alphabetOut;
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
    public Pair<S, List<B>> trans(S si, A x) {
        Pair<S,List<B>> tr = transFn.get(p(si, x));
        return tr;
    }

    @Override
    public Pair<S, List<B>> extTrans(Pair<S, List<B>> si, List<A> w) {
        if (w.isEmpty()) {
            return si;
        }
        else {
            A x = w.get(0); // Java não tem `head`
            List<A> xs = w.subList(1, w.size()); // Java não tem `tail`
            Pair<S,List<B>> sj = trans(si._1, x);
            Pair<S,List<B>> sx = extTrans(sj, xs);
            List<B> bs = append(sj._2, sx._2);
            return p(sx._1, bs);
        }
    }

    @Override
    public boolean accept(List<A> w) {
        Pair<S,List<B>> si = p(initState, new ArrayList<B>());
        Pair<S,List<B>> sx = extTrans(si, w);
        return finalStates.contains(sx._1);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.alphabetIn);
        hash = 53 * hash + Objects.hashCode(this.states);
        hash = 53 * hash + Objects.hashCode(this.transFn);
        hash = 53 * hash + Objects.hashCode(this.initState);
        hash = 53 * hash + Objects.hashCode(this.finalStates);
        hash = 53 * hash + Objects.hashCode(this.alphabetOut);
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
        final MealyMachine<?, ?, ?> other = (MealyMachine<?, ?, ?>) obj;
        if (!Objects.equals(this.alphabetIn, other.alphabetIn)) {
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
        if (!Objects.equals(this.alphabetOut, other.alphabetOut)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MealyMachine{alphabetIn=").append(alphabetIn);
        sb.append(", states=").append(states);
        sb.append(", transFn=").append(transFn);
        sb.append(", initState=").append(initState);
        sb.append(", finalStates=").append(finalStates);
        sb.append(", alphabetOut=").append(alphabetOut);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void writeTo(PrintWriter writer) {
        writer.println("mealy");
        writeCollTo(alphabetIn, writer);
        writer.println();
        writeCollTo(alphabetOut, writer);
        writer.println();
        writeCollTo(states, writer);
        writer.println();
        writer.println(initState);
        writeCollTo(finalStates, writer);
        writer.println();
        transFn.forEach((trigger, destiny) -> {
            writer.printf("%s ", trigger._1);
            writer.printf("%s ", trigger._2);
            writer.printf("%s ", destiny._1);
            writeCollTo(destiny._2, writer);
            writer.println();
        });
    }

}
