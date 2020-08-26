package ifes.data;

/**
 * Uma propriedade ou valor mutável. Esta classe de objetos serve como suporte
 * para a construção de expressões lambda em Java. Em Java, expressões lambda
 * não pode alterar diretamente variáveis locais do escopo onde a expressão
 * lambda foi definida. Por exmeplo, o código abaixo gera erro de compilação:
 * 
 * <pre>{@code
 * public int countEven(Stream<Integer> s) {
 *     int n = 0;
 *     s.forEach(x -> {
 *         if (x %2 == 0) n++;
 *     });
 *     return n;
 * }
 * }</pre>
 * 
 * Esta classe foi criada para contornar esse tipo de problema. Por exemplo,
 * a versão abaixo compila sem erros:
 * 
 * <pre>{@code
 * public int countEven(Stream<Integer> s) {
 *     Property<Integer> n = new Property<>(0);
 *     s.forEach(x -> {
 *         if (x %2 == 0) n.set(n.get() + 1);
 *     });
 *     return n.get();
 * }
 * }</pre>
 *
 * O código fica um pouco mais prolixo (verbose) do que na versão original, mas 
 * não muito.
 * 
 * @author Jefferson Andrade
 */
public class Property<A> {

    private A value;

    public Property(A init) {
        this.value = init;
    }

    public A get() {
        return value;
    }

    public void set(A value) {
        this.value = value;
    }

}
