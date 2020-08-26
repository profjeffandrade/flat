package ifes.flat.run;

import ifes.cli.OptParser;
import ifes.flat.DataFormatException;
import ifes.flat.FsaFactory;
import ifes.flat.rl.Enfa;
import static ifes.flat.run.App.DATA_ERROR;
import static ifes.flat.run.App.IO_ERROR;
import static ifes.flat.run.FlatApp.OPT_DEBUG;
import static ifes.flat.run.FlatApp.OPT_HELP;
import static ifes.flat.run.FlatApp.OPT_INPUT;
import static ifes.flat.run.FlatApp.OPT_OUTPUT;
import static ifes.flat.run.FlatApp.OPT_VERSION;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jefferson
 */
public class EnfaCheckWordApp extends FlatApp {

    public static final String MODULE_NAME = "EnfaCheckWord";
    public static final String MODULE_VERSION = "0.1.1";

    public EnfaCheckWordApp() {
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
                "none"
        );
        return cfg;
    }

    @Override
    public void run(FlatConfig cfg) {
        if (cfg.showHelp) {
            var op = argParser();
            op.writeHelpInfo(cfg.writer);
            cfg.writer.flush();
        } else if (cfg.showVersion) {
            var op = argParser();
            op.writeProgramInfo(cfg.writer);
            cfg.writer.flush();
        } else {
            try {
                Enfa<String, String> m = readEnfa(cfg);
                String t = cfg.reader.readLine();
                while (t != null) {
                    List<String> w = Arrays.asList(t.split(" "));
                    String ans = m.accept(w) ? "ACEITA" : "REJEITA";
                    cfg.writer.println(ans);
                    t = cfg.reader.readLine();
                }
            } catch (IOException ex) {
                System.err.printf("Erro de leitura de dados: %s\n", ex.getMessage());
                System.exit(IO_ERROR);
            } catch (DataFormatException ex) {
                System.err.printf("Erro de leitura de dados: %s\n", ex.getMessage());
                System.exit(DATA_ERROR);
            }
            cfg.writer.flush();
        }
    }

    /**
     * Cria um objeto `Nfa` lendo a definição do AFN à partir da entrada do
     * programa. Lê as linhas da entrada do programa até encontrar uma linha
     * contendo apenas `---`. Estas linhas são salvas em uma string que é usada
     * como argumento para o método que constrói o AFN no objeto
     * <em>factory<em>.
     *
     * @return um objeto `Nfa` construído à partir da definição lida da entrada
     * do programa
     * @throws IOException
     * @throws DataFormatException
     */
    private Enfa<String, String> readEnfa(FlatConfig cfg) throws IOException, DataFormatException {
        StringBuilder sb = new StringBuilder();
        String t = cfg.reader.readLine();
        while (t != null && !t.trim().equals("---")) {
            sb.append(t).append("\n");
            t = cfg.reader.readLine();
        }
        var factory = new FsaFactory();
        Enfa<String, String> m = factory.makeEnfaFromString(sb.toString());
        return m;
    }

    
    @Override
    public OptParser argParser() {
        var parser = new OptParser<FlatConfig>() {
            {
                programName = EnfaCheckWordApp.this.qualifiedModuleName();
                programVersion = EnfaCheckWordApp.MODULE_VERSION;
                usageMessage = "Usage: java -jar Flat.jar eps [options]";
                optList = List.of(
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
