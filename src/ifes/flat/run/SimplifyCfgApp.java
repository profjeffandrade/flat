package ifes.flat.run;

import ifes.Utils;
import static ifes.data.Result.*;
import ifes.cli.OptParser;
import ifes.cli.StrOpt;
import ifes.flat.Grammar;
import ifes.flat.SimpleGrammarParser;
import ifes.flat.cfl.Cfg;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author jefferson
 */
public class SimplifyCfgApp extends FlatApp {

    public static final String MODULE_NAME = "SimplifyCFG";
    public static final String MODULE_VERSION = "0.1.1";

    private static final List<String> actionList = List.of("rus", "rep", "rpsv", "full", "echo");

    public SimplifyCfgApp() {
        super();
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public FlatConfig defaultConfig() {
        var cfg = new FlatConfig(
                false,
                false,
                false,
                new BufferedReader(new InputStreamReader(System.in)),
                new PrintWriter(new OutputStreamWriter(System.out)),
                "full"
        );
        return cfg;
    }

    @Override
    public void run(FlatConfig conf) {
        if (conf.showHelp) {
            var op = argParser();
            op.writeHelpInfo(conf.writer);
            conf.writer.flush();
        } else if (conf.showVersion) {
            var op = argParser();
            op.writeProgramInfo(conf.writer);
            conf.writer.flush();
        } else {
            try {
                var lines = Utils.readLines(conf.reader);
                Grammar<String, String> sg = SimpleGrammarParser.parseGrammar(lines);
                Cfg<String, String> g = new Cfg<>(sg);

                Cfg<String, String> simp = null;

                switch (conf.command) {
                    case "rus":
                        simp = g.removeUselessSymbols();
                        break;
                    case "rep":
                        simp = g.removeEmptyProductions();
                        break;
                    case "rpsv":
                        simp = g.removeSubstituteVariables();
                        break;
                    case "full":
                        simp = g.simplify();
                        break;
                    case "echo":
                        simp = g;
                        break;
                    default:
                        throw new RuntimeException("Unknown command: " + conf.command);
                }
                printResult(conf, simp);
            } catch (IOException ex) {
                System.err.printf("Erro de leitura de dados: %s\n", ex.getMessage());
                System.exit(IO_ERROR);
            }
        }
    }

    /**
     * Imprime a gramática `g` na saída padrão formatada de acordo com a opção
     * de debug selecionada. Se o debug estiver desativado, imprime a gramática
     * formatada como BNF. Caso contrário, imprime a representação completa da
     * gramática.
     *
     * @param g
     */
    private void printResult(FlatConfig conf, Cfg<String, String> g) {
        if (conf.debug) {
            conf.writer.println(g.toString());
        } else {
            g.rules.stream().filter(r -> r.head.contains(g.startSymbol)).forEach(r -> {
                conf.writer.println(r.show());
            });
            g.rules.stream().filter(r -> !r.head.contains(g.startSymbol)).forEach(r -> {
                conf.writer.println(r.show());
            });
        }
        conf.writer.flush();
    }

    /**
     *
     */
    public static final StrOpt<FlatConfig> OPT_ACTION
            = new StrOpt<FlatConfig>("a", "action", "ACTION") {
        {
            description = "Indica a ação que deve ser realizada pelo programa.\n"
                    + "\t As ações (ACTION) podem ser:\n"
                    + "\t `rus` -- remoção de símbolos inúteis;\n"
                    + "\t `rep`  -- remoção de produções vazias;\n"
                    + "\t `rpsv` -- remoção de produções que substituem variáveis;\n"
                    + "\t `full` -- simplificações combinadas (default);\n"
                    + "\t `echo` -- simplesmente repete a gramática de entrada.";
            action = (FlatConfig cfg, String x) -> {
                cfg.command = x;
                return cfg;
            };
            validate = (String x) -> {
                if (x != null && actionList.contains(x)) {
                    return success(x);
                } else if (x == null) {
                    return failure("Ação não informada!");
                } else {
                    return failure("Ação desconhecida: " + x);
                }
            };
        }
    };

    @Override
    public OptParser<FlatConfig> argParser() {
        var parser = new OptParser<FlatConfig>() {
            {
                programName = SimplifyCfgApp.this.qualifiedModuleName();
                programVersion = SimplifyCfgApp.MODULE_VERSION;
                usageMessage = "Usage: java -jar Flat.jar cfg [options]";
                optList = List.of(
                        OPT_ACTION,
                        OPT_HELP,
                        OPT_VERSION,
                        OPT_DEBUG,
                        OPT_INPUT,
                        OPT_OUTPUT
                );
            }
        };
        return parser;
    }

}
