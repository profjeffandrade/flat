package ifes.flat.run;

import ifes.Utils;
import ifes.cli.OptParser;
import ifes.flat.DataFormatException;
import ifes.flat.rl.MealyMachine;
import ifes.flat.rl.MealyMachineReader;
import ifes.flat.rl.MealyMooreTransformer;
import ifes.flat.rl.MooreMachine;
import ifes.flat.rl.MooreMachineReader;
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
public class MealyMooreApp extends FlatApp {

    public static final String MODULE_NAME = "MealyMoore";
    public static final String MODULE_VERSION = "0.1.1";

    public MealyMooreApp() {
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
    public void run(FlatConfig cfg) {
        try {
            var lines = Utils.readLines(cfg.reader);
            switch (lines.get(0)) {
                case "mealy":
                    {
                        var rd = new MealyMachineReader();
                        var m1 = (MealyMachine<String,String,String>)rd.read(lines.subList(1, lines.size()));
                        var m2 = MealyMooreTransformer.mealyToMoore(m1);
                        m2.writeTo(cfg.writer);
                        break;
                    }
                case "moore":
                    {
                        var rd = new MooreMachineReader();
                        var m1 = (MooreMachine<String,String,String>)rd.read(lines.subList(1, lines.size()));
                        var m2 = MealyMooreTransformer.mooreToMealy(m1);
                        m2.writeTo(cfg.writer);
                        break;
                    }
                default:
                    System.err.printf("Erro de leitura de dados. Tipo de máquina inválido: %s\n", lines.get(0));
                    System.exit(DATA_ERROR);
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

    @Override
    public OptParser argParser() {
        var parser = new OptParser<FlatConfig>() {
            {
                programName = MealyMooreApp.this.qualifiedModuleName();
                programVersion = MealyMooreApp.MODULE_VERSION;
                usageMessage = "Usage: java -jar Flat.jar m2m [options]";
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
