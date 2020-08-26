package ifes.flat.run;

import ifes.data.Seq;
import java.util.Map;
import static java.util.Map.entry;
import java.util.function.Supplier;

/**
 *
 * @author Jefferson Andrade
 */
public class Main {
    
    public static final String PROG_NAME = "Flat";
    public static final String PROG_VERSION = "0.1.1";
    
    public static final Map<String, Supplier<App>> apps = Map.ofEntries(
            entry("dfa", () -> new DfaCheckWordApp()),
            entry("nfa", () -> new NfaCheckWordApp()),
            entry("eps", () -> new EnfaCheckWordApp()),
            entry("m2m", () -> new MealyMooreApp()),
            entry("cfg", () -> new SimplifyCfgApp())
    );
    
    
    public static void main(String[] args) {
        Seq<String> argSeq = Seq.from(args);
        if (!argSeq.isEmpty()) {
            var cmd = argSeq.head();
            if (apps.containsKey(cmd)) {
                var app = apps.get(cmd).get();
                var parser = app.argParser();
                var cfg = parser.parseArgs(app.defaultConfig(), argSeq.tail());
                app.run(cfg);
            }
            else {
                showUsage();
            }            
        }
        else {
            showUsage();
        }            
    }
    
    
    public static void showUsage() {
        System.out.println("Usage: java -jar Flat.jar [command] [options]");
        System.out.println("Execute de given command with the options provided.");
        System.out.println("Commands:");
        System.out.println("dfa -- Test if an DFA accepts a given word.");
        System.out.println("nfa -- Test if an NFA accepts a given word.");
        System.out.println("eps -- Test if an NFAɛ accepts a given word.");
        System.out.println("m2m -- Convert between Mealy <-> Moore machines.");
        System.out.println("cfg -- Simplfy a context free grammar.");
        System.out.println("For help on each command use:");
        System.out.println("java -jar Flat.jar [command] -h|--help");
        System.out.println();
    }
    
}
