package ifes.flat.run;

import ifes.cli.OptParser;
import ifes.cli.Config;

/**
 * Classe básica para os módulos do programa de manipulação de lingugens e
 * autômatos. Todos os módulos que forem acrescentados ao programa devem ser
 * especializações desta classe.
 *
 * @author Jefferson Andrade.
 * @param <C> classe de configuração utilizada pelo módulo
 */
public abstract class App<C extends Config> {

    public static final String NAME_SEP = "::";
    
    // Códigos de erro comuns utilizados pelos programas
    public static final int DATA_ERROR = 65;
    public static final int NO_INPUT_ERROR = 66;
    public static final int CANT_CREATE_ERROR = 73;
    public static final int IO_ERROR = 74;
    
    public abstract C defaultConfig();

    public abstract OptParser<C> argParser();

    public abstract void run(C cfg);
    
    public abstract String moduleName();
    
    public String qualifiedModuleName() {
        return Main.PROG_NAME + NAME_SEP + moduleName();
    }
    
}
