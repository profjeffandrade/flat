package ifes.flat.rl;

import ifes.data.Pair;
import static ifes.data.Pair.p;
import static ifes.Utils.append;
import static ifes.Utils.writeCollTo;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ifes.flat.Fsa;

/**
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> tipo dos objetos que representam estados do autômato
 * @param <A> tipo dos objetos que representam símbolos do alfabeto de entrada
 * @param <B> tipo dos objetos que representam símbolos do alfabeto de saída
 */
public class MooreMachine<S, A, B> implements Fsa<S, A, S, A> {

    private final Set<A> alphabetIn;
    private final Set<S> states;
    private final Map<Pair<S, A>, S> transFn;
    private final S initState;
    private final Set<S> finalStates;
    private final Set<B> alphabetOut;
    private final Map<S, List<B>> outputFn;

    public MooreMachine(
            Set<A> alphabetInt, 
            Set<S> states, 
            Map<Pair<S,A>, S> transFn,
            S initState, 
            Set<S> finalStates,
            Set<B> alphabetOut,
            Map<S,List<B>> ouputFn) {
        this.alphabetIn = Collections.unmodifiableSet(alphabetInt);
        this.states = Collections.unmodifiableSet(states);
        this.transFn = Collections.unmodifiableMap(transFn);
        this.initState = initState;
        this.finalStates = Collections.unmodifiableSet(finalStates);
        this.alphabetOut = Collections.unmodifiableSet(alphabetOut);
        this.outputFn = Collections.unmodifiableMap(ouputFn);
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
    public S trans(S si, A x) {
        S sj = transFn.get(p(si, x));
        return sj;
    }

    @Override
    public S extTrans(S si, List<A> w) {
        if (w.isEmpty()) {
            return si;
        }
        else {
            A x = w.get(0); // Java não tem `head`
            List<A> xs = w.subList(1, w.size()); // Java não tem `tail`
            S sj = trans(si, x);
            return extTrans(sj, xs);
        }
    }
    
    public List<B> outTrans(S si, List<A> w) {
        return outTrans(si, w, Collections.emptyList());
    }
    
    public List<B> outTrans(S si, List<A> w, List<B> acc) {
        if (w.isEmpty()) {
            return outputFn.get(si);
        }
        else {
            A x = w.get(0); // Java não tem `head`
            List<A> xs = w.subList(1, w.size()); // Java não tem `tail`
            S sj = trans(si, x);
            return outTrans(sj, xs, append(acc, outputFn.get(si)));
        }
    }

    @Override
    public boolean accept(List<A> w) {
        S sx = extTrans(initState, w);
        return finalStates.contains(sx);
    }

    @Override
    public void writeTo(PrintWriter writer) {
        writer.println("moore");
        writeCollTo(alphabetIn, writer);
        writer.println();
        writeCollTo(alphabetOut, writer);
        writer.println();
        writeCollTo(states, writer);
        writer.println();
        writer.println(initState);
        writeCollTo(finalStates, writer);
        writer.println();
        outputFn.forEach((si, output) -> {
            writer.printf("%s ", si);
            writeCollTo(output, writer);
            writer.println();
        });
        transFn.forEach((trigger, sj) -> {
            writer.printf("%s %s %s\n", trigger._1, trigger._2, sj);
        });
    }
    
}
