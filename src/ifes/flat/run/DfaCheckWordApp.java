package ifes.flat.run;

import ifes.cli.OptParser;
import ifes.flat.DataFormatException;
import ifes.flat.FsaFactory;
import ifes.flat.rl.Dfa;
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
public class DfaCheckWordApp extends FlatApp {

    public static final String MODULE_NAME = "DfaCheckWord";
    public static final String MODULE_VERSION = "0.1.1";

    public DfaCheckWordApp() {
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
            Dfa<String, String> m;
            try {
                m = readDfa(cfg);
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
     * Cria um objeto `Dfa` lendo a definição do AFD à partir da entrada do
     * programa. Lê as linhas da entrada do programa até encontrar uma linha
     * contendo apenas `---`. Estas linhas são salvas em uma string que é usada
     * como argumento para o método que constrói o AFD no objeto
     * <em>factory<em>.
     *
     * @return um objeto `Dfa` construído à partir da definição lida da entrada
     * do programa
     * @throws IOException
     * @throws DataFormatException
     */
    private Dfa<String, String> readDfa(FlatConfig cfg) throws IOException, DataFormatException {
        StringBuilder sb = new StringBuilder();
        String t = cfg.reader.readLine();
        while (t != null && !t.trim().equals("---")) {
            sb.append(t).append("\n");
            t = cfg.reader.readLine();
        }
        var factory = new FsaFactory();
        Dfa<String, String> m = factory.makeDfaFromString(sb.toString());
        return m;
    }

    @Override
    public OptParser<FlatConfig> argParser() {
        var parser = new OptParser<FlatConfig>() {
            {
                programName = DfaCheckWordApp.this.qualifiedModuleName();
                programVersion = DfaCheckWordApp.MODULE_VERSION;
                usageMessage = "Usage: java -jar Flat.jar dfa [options]";
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
