package ifes.cli;

import ifes.data.Result;
import ifes.data.Seq;
import static ifes.data.Unit.UNIT;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Um parser para argumentos de linha de comando.O parser é construído
 * definindo-se "opções" de linha de comando. Essas opções podem ser comandos
 * (definidos apenas por um nome, sem traços antes do nome), flag (com nomes
 * curtos e nomes longos, com traços antes dos nome, sendo um traço para nome
 * curto e dois traços para nomes longos), ou opções com valores (com nomes
 * curtos e nomes longos assim como as flags). Depois que o construtor é criado,
 * é necessário chamar o método {@code sequence} para definir as opções.
 *
 * @author Jefferson Andrade
 * @param <C> tipo do objeto de configuração que será gerado pelo parser
 */
public class OptParser<C extends Config> {

    public String programName;
    public String programVersion;
    public Integer programYear;
    public String authorName;
    public String authorEmail;
    public String headerMessage;
    public String footerMessage;
    public String usageMessage;
    public List<Opt<C, ?>> optList;

    public OptParser() {
        this.programName = null;
        this.programVersion = null;
        this.programYear = null;
        this.authorName = null;
        this.authorEmail = null;
        this.headerMessage = null;
        this.footerMessage = null;
        this.usageMessage = null;
        this.optList = Collections.emptyList();
    }

    public Config parseArgs(C cfg, Seq<String> args) {
        Seq<String> curr = args;
        List<Opt<C, ?>> foundParams = new ArrayList<>();
        boolean stop = false;

        while (!stop && !curr.isEmpty()) {
            Seq<String> prev = curr;
            boolean match = false;
            Iterator<Opt<C, ?>> pit = optList.iterator();

            while (pit.hasNext() && !match) {
                Opt<C, Object> opt = (Opt<C, Object>) pit.next();
                String value = null;
                if (opt.matchShortName(curr.head())) {
                    match = true;
                    if (opt.needValue() && curr.length() > 1) {
                        curr = curr.tail();
                        value = curr.head();
                    }
                } else if (opt.matchLongName(curr.head())) {
                    match = true;
                    if (opt.needValue()) {
                        value = opt.getLongNameValue(curr.head()).orElse(null);
                    }
                }

                if (match) {
                    foundParams.add(opt);
                    if (opt.isCommand() || opt.isFlag()) {
                        cfg = opt.action.apply(cfg, UNIT);
                    } else {
                        Object x = opt.convert.apply(value);
                        Result<?> result = opt.validate.test(x);
                        if (result.isFailure()) {
                            throw new RuntimeException("Validation error: " + result.getMessage());
                        }
                        cfg = opt.action.apply(cfg, x);
                    }
                }
            }
            stop = (curr == prev);
            curr = curr.tail();
        }
        cfg.rest = curr;
        return cfg;
    }

    public void writeHelpInfo(Writer writer) {
        PrintWriter wr = new PrintWriter(writer);
        writeUsageInfo(wr);
        writeProgramInfo(wr);
        writeAuthorInfo(wr);
        writeHeaderInfo(wr);
        writeCommandsInfo(wr);
        writeOptionsInfo(wr);
        writeFooterInfo(wr);
    }

    public void writeProgramInfo(PrintWriter wr) {
        if (programName != null) {
            if (programVersion != null) {
                wr.printf("%s version %s. ", programName, programVersion);
            } else {
                wr.printf("%s. ", programName);
            }
        }
        wr.println();
    }

    public void writeAuthorInfo(PrintWriter wr) {
        if (authorName != null) {
            if (authorEmail != null) {
                wr.printf("Created by: %s (%s)", authorName, authorEmail);
            } else {
                wr.printf("Created by: %s", authorName, authorEmail);
            }
            if (programYear != null) {
                wr.printf(" -- %d", programYear);
            }
            wr.println(".");
        }
    }

    public void writeUsageInfo(PrintWriter wr) {
        if (usageMessage != null) {
            wr.println(usageMessage);
        }
    }

    public void writeCommandsInfo(PrintWriter wr) {
        List<Opt<C, ?>> cmds = optList.stream().filter(o -> o.isCommand()).collect(Collectors.toList());
        if (!cmds.isEmpty()) {
            wr.println("COMMANDS:");
            cmds.forEach(opt -> {
                wr.printf("    %s:\t%s\n",
                        opt.shortName, String.valueOf(opt.description));
            });
            wr.println();
        }
    }

    public void writeOptionsInfo(PrintWriter wr) {
        List<Opt<C, ?>> opts = optList.stream().filter(o -> !o.isCommand()).collect(Collectors.toList());
        if (!opts.isEmpty()) {
            wr.println("OPTIONS:");
            opts.forEach(opt -> {
                if (opt.isFlag()) {
                    wr.printf("    %s | %s\n",
                            opt.dashShortName(), opt.dashLongName());
                    wr.printf("\t%s\n", opt.description);
                } else {
                    wr.printf("    %s %s | %s=%s\n",
                            opt.dashShortName(), opt.valueName,
                            opt.dashLongName(), opt.valueName);
                    wr.printf("\t%s\n", opt.description);
                }
            });
            wr.println();
        }
    }

    private void writeHeaderInfo(PrintWriter wr) {
        if (headerMessage != null) {
            wr.println(headerMessage);
        }
    }

    private void writeFooterInfo(PrintWriter wr) {
        if (footerMessage != null) {
            wr.println(footerMessage);
        }
    }
}
