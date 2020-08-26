package ifes.flat;

import ifes.flat.rl.Dfa;
import ifes.flat.rl.DfaReader;
import ifes.flat.rl.MealyMachine;
import ifes.flat.rl.MealyMachineReader;
import ifes.flat.rl.MooreMachine;
import ifes.flat.rl.MooreMachineReader;
import ifes.flat.rl.Nfa;
import ifes.flat.rl.Enfa;
import ifes.flat.rl.EnfaReader;
import ifes.flat.rl.NfaReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Uma fábrica utilizada para crianção de instâncias de <em>autômatos
 * finitos</em>.
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 */
public class FsaFactory {

    /**
     * Converte o conteúdo do arquivo indicado, representando um AFD, em uma
     * instância de `Dfa`.Veja `makeDfaFromString` para a descrição do formato
     * da definição do AFD.
     *
     * @param fname nome e caminho do arquivo contendo a definição do AFD
     * @return uma instância de `Dfa`
     * @throws ifes.lfa.af.DataFormatException
     * @throws java.io.IOException
     */
    public Dfa<String, String> makeDfaFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeDfaFromString(contents);
    }

    /**
     * Converte a string dada, representando um AFD, em uma instância de `Dfa`.
     * A definição do AFD é segue a forma esquemática dada a seguir, por linha
     * de texto:
     * <ol>
     * <li>A string `dfa`.</li>
     * <li>Os símbolos do alfabeto, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino`.</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de uma classe `Dfa`
     */
    public Dfa<String, String> makeDfaFromString(String t) throws DataFormatException {
        String[] ts = t.split("\n");
        if (ts.length < 5) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }
        if (!ts[0].equals("dfa")) {
            throw new DataFormatException("Definição não corresponde a AFD");
        }
        List<String> lines = Arrays.asList(ts);
        var rd = new DfaReader();
        var m1 = rd.read(lines.subList(1, lines.size()));
        return (Dfa<String, String>) m1;
    }

    /**
     * Converte o conteúdo do arquivo indicado, representando um AFN, em uma
     * instância de `Nfa`.Veja `makeNfaFromString` para a descrição do formato
     * da definição do AFN.
     *
     * @param fname nome e caminho do arquivo contendo a definição do AFN
     * @return uma instância da classe `Nfa`
     * @throws ifes.lfa.af.DataFormatException
     * @throws java.io.IOException
     */
    public Nfa<String, String> makeNfaFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeNfaFromString(contents);
    }

    /**
     * Converte a string dada, representando um AFN, em uma instância de `Nfa`.
     * A definição do AFN é segue a forma esquemática dada a seguir, por linha
     * de texto:
     * <ol>
     * <li>A string `nfa`.</li>
     * <li>Os símbolos do alfabeto, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino1` `estado-destino2` ...</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de uma classe que implementa a interface `Nfa`
     * @throws ifes.lfa.af.DataFormatException
     */
    public Nfa<String, String> makeNfaFromString(String t) throws DataFormatException {
        String[] ts = t.split("\n");
        if (ts.length < 5) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }
        if (!ts[0].equals("nfa")) {
            throw new DataFormatException("Definição não corresponde a AFN");
        }
        List<String> lines = Arrays.asList(ts);
        var rd = new NfaReader();
        var m1 = rd.read(lines.subList(1, lines.size()));
        return (Nfa<String, String>) m1;
    }

    /**
     * Converte o conteúdo do arquivo indicado, representando um AFNe, em uma
     * instância de `Enfa`.Veja `makeEnfaFromString` para a descrição do formato
     * da definição do AFNe.
     *
     * @param fname nome e caminho do arquivo contendo a definição do AFNe
     * @return Um objeto representando o autômato finito com movimentos vazios
     * descrito no arquivo.
     * @throws java.io.IOException
     */
    public Enfa<String, String> makeEnfaFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeEnfaFromString(contents);
    }

    /**
     * Converte a string dada, representando um AFNe, em uma instância de
     * `Enfa`. A definição do AFNe é segue a forma esquemática dada a seguir,
     * por linha de texto:
     * <ol>
     * <li>A string `nfae`.</li>
     * <li>Os símbolos do alfabeto, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino1` `estado-destino2` ...</br>
     * (Transiçoes vazias são representadas pelo símbolo `_`.)</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de uma classe que implementa a interface `Nfa`
     * @throws ifes.lfa.af.DataFormatException
     */
    public Enfa<String, String> makeEnfaFromString(String t) throws DataFormatException {
        String[] ts = t.split("\n");
        if (ts.length < 5) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }
        if (!ts[0].equals("nfae")) {
            throw new DataFormatException("Definição não corresponde a AFNe");
        }
        List<String> lines = Arrays.asList(ts);
        var rd = new EnfaReader();
        var m1 = rd.read(lines.subList(1, lines.size()));
        return (Enfa<String, String>) m1;
    }

    /**
     * Converte o conteúdo do arquivo indicado, representando uma Máquina de
     * Mealy, em uma instância de `MealyMachine`. Veja `makeMealyFromString`
     * para a descrição do formato da definição da Máquina de Mealy.
     *
     * @param fname nome e caminho do arquivo contendo a definição da Máquina de
     * Mealy
     * @return Um objeto representando a Máquina de Mealy
     * @throws ifes.lfa.af.DataFormatException
     * @throws java.io.IOException
     */
    public MealyMachine<String, String, String> makeMealyFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeMealyFromString(contents);
    }

    /**
     * Converte a string dada, representando uma Máquina de Mealy , em uma
     * instância de `MealyMachine`. A definição da Máquina de Mealy segue a
     * forma esquemática dada a seguir, por linha de texto:
     * <ol>
     * <li>A string `mealy`.</li>
     * <li>Os símbolos do alfabeto de entrada, separados por espaço.</li>
     * <li>Os símbolos do alfabeto de saída, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino1` `estado-destino2`
     * `símbolo-saída1` `símbolo-saída2` ...</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de classe `MealyMachine`
     * @throws ifes.lfa.af.DataFormatException
     */
    public MealyMachine<String, String, String> makeMealyFromString(String t) throws DataFormatException {
        String[] ts = t.split("\n");
        if (ts.length < 6) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }
        if (!ts[0].equals("mealy")) {
            throw new DataFormatException("Definição não corresponde a Máquina de Mealy.");
        }
        List<String> lines = Arrays.asList(ts);
        var rd = new MealyMachineReader();
        var m1 = rd.read(lines.subList(1, lines.size()));
        return (MealyMachine<String, String, String>) m1;
    }

    /**
     * Converte o conteúdo do arquivo indicado, representando uma Máquina de
     * Moore, em uma instância de `MooreMachine`. Veja `makeMooreFromString`
     * para a descrição do formato da definição da Máquina de Moore.
     *
     * @param fname nome e caminho do arquivo contendo a definição da Máquina de
     * Mealy
     * @return Um objeto representando a Máquina de Moore
     * @throws ifes.lfa.af.DataFormatException
     * @throws java.io.IOException
     */
    public MooreMachine<String, String, String> makeMooreFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeMooreFromString(contents);
    }

    /**
     * Converte a string dada, representando uma Máquina de Moore, em uma
     * instância de `MooreMachine`. A definição da Máquina de Moore segue a
     * forma esquemática dada a seguir, por linha de texto:
     * <ol>
     * <li>A string `moore`.</li>
     * <li>Os símbolos do alfabeto de entrada, separados por espaço.</li>
     * <li>Os símbolos do alfabeto de saída, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Função de saída da Máquina de Moore, haverá uma linha por estado.
     * Cada linha da função de saída dos estados tem o seguinte formato:</br>
     * `estado` `símbolo-saída1` `símbolo-saída2` ...</br>
     * (Note que pode não haver nenhum símbolo de saída para um estado, mas a
     * linha correspondente a ele deve aparecer. Se não houer saída a linha terá
     * apenas o nome do estado.)</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino1` `estado-destino2`
     * `símbolo-saída1` `símbolo-saída2` ...</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de uma classe `MooreMachine`
     * @throws ifes.lfa.af.DataFormatException
     */
    public MooreMachine<String, String, String> makeMooreFromString(String t) throws DataFormatException {
        String[] ts = t.split("\n");
        if (ts.length < 6) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }
        if (!ts[0].equals("mealy")) {
            throw new DataFormatException("Definição não corresponde a Máquina de Mealy.");
        }
        List<String> lines = Arrays.asList(ts);
        var rd = new MooreMachineReader();
        var m1 = rd.read(lines.subList(1, lines.size()));
        return (MooreMachine<String, String, String>) m1;
    }

}
