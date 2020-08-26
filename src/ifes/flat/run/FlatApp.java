package ifes.flat.run;

import ifes.cli.FileOpt;
import ifes.cli.Flag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jefferson
 */
public abstract class FlatApp extends App<FlatConfig> {

    // ====================================================================
    // Definição das opções de linha de comando comuns à todos os módulos
    /**
     *
     */
    public static final Flag<FlatConfig> OPT_HELP
            = new Flag<FlatConfig>("h", "help") {
        {
            description = "mostra informações de ajuda e modo de uso";
            action = (FlatConfig cfg, Object x) -> {
                cfg.showHelp = true;
                return cfg;
            };
        }
    };

    /**
     *
     */
    public static final Flag<FlatConfig> OPT_VERSION
            = new Flag<FlatConfig>("v", "version") {
        {
            description = "imprime a versão do programa";
            action = (FlatConfig cfg, Object x) -> {
                cfg.showVersion = true;
                return cfg;
            };
        }
    };

    /**
     *
     */
    public static final Flag<FlatConfig> OPT_DEBUG
            = new Flag<FlatConfig>("d", "debug") {
        {
            description = "imprime informações de depuração";
            action = (FlatConfig cfg, Object x) -> {
                cfg.debug = true;
                return cfg;
            };
        }
    };

    /**
     *
     */
    public static final FileOpt<FlatConfig> OPT_INPUT
            = new FileOpt<FlatConfig>("i", "input", "FILE") {
        {
            description = "Lê os dados do arquivo FILE."
                    + " Se não for especificado, lê da entrada padrão.";
            action = (FlatConfig cfg, File f) -> {
                try {
                    cfg.reader = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SimplifyCfgApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                return cfg;
            };
        }
    };

    /**
     *
     */
    public static final FileOpt<FlatConfig> OPT_OUTPUT
            = new FileOpt<FlatConfig>("o", "output", "FILE") {
        {
            description = "Escreve os dados no arquivo FILE."
                    + " Se não for especificado, escreve na saída padrão.";
            action = (FlatConfig cfg, File f) -> {
                try {
                    cfg.writer = new PrintWriter(new FileWriter(f));
                } catch (IOException ex) {
                    Logger.getLogger(SimplifyCfgApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                return cfg;
            };
        }
    };

}
