package ifes.data;

import static ifes.data.Pair.p;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Implementa uma lista simplesmente encadeada semelhante às listas padrão em
 * Lisp, Haskell ou Scala.A lista criada é imutável, e pode ser convertida para
 * listas de Java pelo método `toList`, ou para streams pelo método `toStream`.
 *
 * @author Jefferson Andrade
 * @param <T> Tipo de dados armazenados na lista
 */
public abstract class Seq<T> {

    /**
     * Cria uma lista vazia. Diferente de Lisp ou Scala, não existe um objeto
     * único para representar a lista vazia. Isso se deve principalmente ao fato
     * de Java não ter um tipo como o `Nothing` em Scala.
     *
     * @param <S> Tipo de dados da lista vazia.
     * @return um objeto representando a lista vazia.
     */
    public static <S> Seq<S> empty() {
        return new Nil<>();
    }

    /**
     * Constrói uma lista à partir de um elemento `x` e de uma lista `xs` já
     * existente.
     *
     * @param <S> Tipo de dados da lista e do elemento
     * @param x elemento que será a cabeça da nova lista
     * @param xs lista já existente, que será a calda da nova lista
     * @return uma nova lista tendo `x` como cabeça e `xs` como calda
     */
    public static <S> Seq<S> cons(S x, Seq<S> xs) {
        return new Cons(x, xs);
    }

    /**
     * Cria uma nova lista à partir de um vetor de elementos.
     *
     * @param <S> Tipo de dados dos elementos da lista
     * @param elems vetor que gerará a lista
     * @return uma nova lista criada à partir dos elementos de `elems`
     */
    public static <S> Seq<S> from(S[] elems) {
        Builder<S> b = new Builder<>();
        for (int i = 0; i < elems.length; i++) {
            b.add(elems[i]);
        }
        return b.build();
    }

    /**
     * Cria uma nova lista à partir de um vetor de elementos especificados como
     * um argumento “vararg”.
     *
     * @param <S> Tipo de dados dos elementos da lista
     * @param elems vetor que gerará a lista
     * @return uma nova lista criada à partir dos elementos de `elems`
     */
    public static <S> Seq<S> of(S... elems) {
        return from(elems);
    }

    /**
     * Cria uma nova lista à partir de uma lista padrão de Java.
     *
     * @param <S> Tipo de dados dos elementos da lista
     * @param elems lista padrão de Java, à partir da qual será gerada a lista
     * encadeada
     * @return uma nova lista criada à partir dos elementos de `elems`
     */
    public static <S> Seq<S> from(List<S> elems) {
        Builder<S> b = new Builder<>();
        Iterator<S> it = elems.iterator();
        while (it.hasNext()) {
            b.add(it.next());
        }
        return b.build();
    }

    /**
     * Retorna uma lista formada pela concatenação de todas as listas em `xss`.
     *
     * @param <S> o tipo de dados dos elementos das lista a serem concatenadas
     * @param xss o vetor com as listas que serão concatenadas
     * @return uma lista formada pela concatenação de todas as listas em `xss`
     */
    public <S> Seq<S> concat(Seq<S>... xss) {
        Builder<S> b = new Builder<>();
        for (Seq<S> xs : xss) {
            b.addAll(xs);
        }
        return b.build();
    }

    /**
     * Retorna um lista de número inteiros contendo todos os elementos `i`, tais
     * que `begin &lt; i &lt; i + step &lt end`. Caso o valor do passo seja
     * negativo, os elementos da lista são definidos como `end &lt; i - |step|
     * &lt; i &lt; begin`.
     *
     * @param begin limite inferior da lista
     * @param end limite superior da lista
     * @param step intervalo entre os elementos da lista
     * @return a lista com os número inteiros entre `begin` e `end` separados
     * por uma distância `step`.
     */
    public static Seq<Integer> range(int begin, int end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step cannot be zero.");
        }
        Builder<Integer> b = new Builder<>();
        if (step > 0 && begin < end) {
            for (int i = begin; i < end; i += step) {
                b.add(i);
            }
        } else if (step < 0 && begin > end) {
            for (int i = begin + step; i > end; i += step) {
                b.add(i);
            }
        }
        return b.build();
    }

    /**
     * Retorna um lista de número inteiros contendo todos os elementos `i`, tais
     * que `begin &lt; i &lt; i + 1 &lt end`.
     *
     * @param begin limite inferior da lista
     * @param end limite superior da lista
     * @return a lista com os número inteiros entre `begin` e `end` separados
     * por uma distância 1.
     */
    public static Seq<Integer> range(int begin, int end) {
        return range(begin, end, 1);
    }

    /**
     * Retorna um lista de número inteiros contendo todos os elementos `i`, tais
     * que `0 &lt; i &lt; i + 1 &lt end`.
     *
     * @param end limite superior da lista
     * @return a lista com os número inteiros entre 0 e `end` separados por uma
     * distância 1.
     */
    public static Seq<Integer> range(int end) {
        return range(0, end, 1);
    }

    /**
     * Indica se esta lista é vazia ou não.
     *
     * @return `true` se a lista for vazia, e `false` caso contrário
     */
    public abstract boolean isEmpty();

    /**
     * Retorna o primeiro elemento da lista. Gera um erro de tempo de execução
     * se a lista for vazia.
     *
     * @return o primeiro elemento da lista
     */
    public abstract T head();

    /**
     * Retorna a "cauda" da lista, i.e., a lista menos o primeiro elemento. Gera
     * um erro de tempo de execução se a lista for vazia.
     *
     * @return a cauda da lista
     */
    public abstract Seq<T> tail();

    /**
     * Retorna o número de elementos da lista.
     *
     * @return o número de elementos da lista
     */
    public int length() {
        Counter len = new Counter();
        this.forEach(x -> len.inc());
        return len.get();
    }
    
    /**
     * Testa se a lista contém um elementos.
     *
     * @return verdadeiro se a lista contém o elemento, falso caso contrário
     */
    public boolean contains(T elem) {
        return exists(x -> x.equals(elem));
    }

    /**
     * Retorna uma nova lista, contendo os mesmos elementos que esta lista,
     * porém em ordem invertida.
     *
     * @return a lista invertida
     */
    public Seq<T> reverse() {
        Seq<T> acc = empty();
        Seq<T> curr = this;
        while (!curr.isEmpty()) {
            acc = cons(curr.head(), acc);
            curr = curr.tail();
        }
        return acc;
    }

    /**
     * Retorna uma nova lista formada pelos `n` primeiros elementos desta lista.
     * Se esta lista tiver menos do que `n` elementos, retorna uma nova lista
     * com todos os elementos.
     *
     * @param n número de elementos do início da lista que devem ser retornados
     * @return uma lista com os `n` primeiros elementos desta lista
     */
    public Seq<T> take(int n) {
        Builder<T> b = new Builder<>();
        Seq<T> curr = this;
        while (!curr.isEmpty() && n > 0) {
            b.add(curr.head());
            curr = curr.tail();
            n -= 1;
        }
        return b.build();
    }

    /**
     * Retorna uma nova lista com os elementos desta lista da posição `n` em
     * diante. Se a lista tiver uma quantidade de elementos menor ou igual a
     * `n`, retorna a lista vazia.
     *
     * @param n a quantidade de elementos que devem ser "descartados"
     * @return uma nova lista com os elementos da posição `n` em diante
     */
    public Seq<T> drop(int n) {
        Seq<T> curr = this;
        while (!curr.isEmpty() && n > 0) {
            curr = curr.tail();
            n -= 1;
        }
        return curr;
    }

    /**
     * Retorna uma nova lista formada pela concatenação dos elementos desta
     * lista com os elementos da lista `s2`.
     *
     * @param s2 lista com a qual esta lista deve ser concatenada
     * @return uma lista formada pela concatenação desta lista com `s2`
     */
    public Seq<T> append(Seq<T> s2) {
        return concat(this, s2);
    }

    /**
     * Retorna uma nova lista formada por todos os elementos para os quais o
     * predicado `p` retorna <b>verdadeiro</b>.
     *
     * @param p predicado usado para testar os elementos
     * @return uma lista formada apenas pelos elementos que satisfazem `p`
     */
    public Seq<T> filter(Predicate<T> p) {
        Builder<T> b = new Builder<>();
        this.forEach(x -> {
            if (p.test(x)) {
                b.add(x);
            }
        });
        return b.build();
    }

    /**
     * Retorna uma lista formada por todos os elementos para os quais o
     * predicado `p` retorna <b>falso</b>.
     *
     * @param p predicado usado para testar os elementos
     * @return uma lista formada apenas pelos elementos que não satisfazem `p`
     */
    public Seq<T> filterNot(Predicate<T> p) {
        return filter(x -> !p.test(x));
    }

    /**
     * Retorna verdadeiro se todos os elementos desta lista satisfazem o
     * predicado `p`, e falso caso contrário.
     *
     * @param p predicado usado para testar os elementos
     * @return verdadeiro se todos os elementos satisfazem `p`, falso caso
     * contrário
     */
    public boolean forAll(Predicate<T> p) {
        boolean all = true;
        Iterator<T> it = iterator();
        while (all && it.hasNext()) {
            all = all && p.test(it.next());
        }
        return all;
    }

    /**
     * Retorna verdadeiro se algum elemento desta lista satisfaz o predicado
     * `p`, e falso caso contrário.
     *
     * @param p predicado usado para testar os elementos
     * @return verdadeiro se algum elemento satisfaz `p`, falso caso contrário
     */
    public boolean exists(Predicate<T> p) {
        boolean any = false;
        Iterator<T> it = iterator();
        while (!any && it.hasNext()) {
            any = any || p.test(it.next());
        }
        return any;
    }

    /**
     * Executa a função `f` para cada elemento desta lista.
     *
     * @param f função que será executada para cada elemento
     */
    public void forEach(Consumer<T> f) {
        Seq<T> curr = this;
        while (!curr.isEmpty()) {
            f.accept(curr.head());
            curr = curr.tail();
        }
    }

    /**
     * Retorna uma nova lista formada pela aplicação da função `f` a cada
     * elementos desta lista. Por exemplo, se esta lista for `[x0 x1 x2 ...]` a
     * nova lista será `[f(x0) f(x1) f(x2) ...]`.
     *
     * @param <S> o tipo de retorno da função `f`
     * @param f a função que será aplicada a cada elemento da lista
     * @return uma lista nova formada pelos resultados da aplicação de `f` aos
     * elementos desta lista
     */
    public <S> Seq<S> map(Function<T, S> f) {
        Builder<S> b = new Builder<>();
        this.forEach(x -> b.add(f.apply(x)));
        return b.build();
    }

    /**
     * Retorna uma nova lista formada pela concatenação da aplicação da função
     * `f` a cada elementos desta lista. A função `f` quando aplicada a cada
     * elementos desta lista retornará uma lista de elementos do tipo `S`. Esta
     * listas retornadas por `f` serão concatenadas para retornar o resultado
     * final do `flatMap`. Por exemplo, se esta lista for `[x0 x1 x2 ...]` a
     * nova lista será equivalente a `concat(f(x0), f(x1), f(x2), ...)`.
     *
     * @param <S> o tipo dos elementos das lista retornadas por `f`
     * @param f a função que será aplicada a cada elemento da lista
     * @return uma lista nova formada pela concatenação dos resultados da
     * aplicação de `f` aos elementos desta lista
     */
    public <S> Seq<S> flatMap(Function<T, Seq<S>> f) {
        Builder<S> b = new Builder<>();
        this.forEach(x -> b.addAll(f.apply(x)));
        return b.build();
    }

    /**
     * Realiza uma transformação acumulativa dos elementos desta lista de acordo
     * com uma função binária `f` e um valor inicial `init`. A transformação é
     * realizada da esquerda para a direita. Por exemplo, caso esta lista fosse
     * `[x0, x1, x2, x3]`, o resultado da transformação seria equivalente a
     * `f(f(f(f(init, x0), x1), x2), x3)`.
     *
     * @param <A> o tipo de dados do valor inicial e do retorno da função `f`
     * @param init o valor inicial da redução
     * @param f a função binária que realiza a redução de cada estágio do
     * cálculo
     * @return uma redução do elementos desta lista segundo `f` e `init`
     */
    public <A> A foldLeft(A init, BiFunction<A, T, A> f) {
        Property<A> acc = new Property<>(init);
        this.forEach(x -> acc.set(f.apply(acc.get(), x)));
        return acc.get();
    }

    /**
     * Realiza uma transformação acumulativa dos elementos desta lista de acordo
     * com uma função binária `f` e um valor inicial `init`. A transformação é
     * realizada da direita para a esquerda. Por exemplo, caso esta lista fosse
     * `[x0, x1, x2, x3]`, o resultado da transformação seria equivalente a
     * `f(x0, f(x1, f(x2, f(x3, init))))`.
     *
     * @param <A> o tipo de dados do valor inicial e do retorno da função `f`
     * @param init o valor inicial da redução
     * @param f a função binária que realiza a redução de cada estágio do
     * cálculo
     * @return uma redução do elementos desta lista segundo `f` e `init`
     */
    public <A> A foldRight(A init, BiFunction<T, A, A> f) {
        ArrayList<T> buffer = new ArrayList<>();
        this.forEach(x -> buffer.add(x));
        A acc = init;
        for (int i = buffer.size() - 1; i >= 0; i--) {
            acc = f.apply(buffer.get(i), acc);
        }
        return acc;
    }

    /**
     * Retorna um valor opcional correspondente à redução dos elementos desta
     * lista de acordo com uma função binária `f`. A redução é realizada da
     * esquerda para a direita. Por exemplo, se a lista for `[x0, x1, x2, x3]`,
     * o resultado será equivalente a `f(f(f(x0, x1), x2), x3)`. Caso a lista
     * seja vazia, retorna um opcinal vazio.
     *
     * @param f a função binária que realiza a redução de cada estágio do
     * cálculo
     * @return uma redução do elementos desta lista segundo `f` e `init`
     */
    public Optional<T> reduceLeft(BiFunction<T, T, T> f) {
        if (this.isEmpty()) {
            return Optional.empty();
        } else {
            T x = tail().foldLeft(head(), f);
            return Optional.of(x);
        }
    }

    /**
     * Retorna um valor opcional correspondente à redução dos elementos desta
     * lista de acordo com uma função binária `f`. A redução é realizada da
     * direita para a esquerda. Por exemplo, se a lista for `[x0, x1, x2, x3]`,
     * o resultado seria equivalente a `f(x0, f(x1, f(x2, x3)))`. Caso a lista
     * seja vazia, retorna um opcinal vazio.
     *
     * @param f a função binária que realiza a redução de cada estágio do
     * cálculo
     * @return uma redução do elementos desta lista segundo `f` e `init`
     */
    public Optional<T> reduceRight(BiFunction<T, T, T> f) {
        if (this.isEmpty()) {
            return Optional.empty();
        } else {
            ArrayList<T> buffer = new ArrayList<>();
            this.forEach(x -> buffer.add(x));
            T acc = buffer.get(buffer.size() - 1);
            for (int i = buffer.size() - 2; i >= 0; i--) {
                acc = f.apply(buffer.get(i), acc);
            }
            return Optional.of(acc);
        }
    }

    /**
     * Agrupa os elementos desta lista de acordo com o valor retornado pela
     * função `f` para cada elemento. A função `f` será aplicada a cada elemento
     * desta lista; elementos para os quais `f` retornar um mesmo valor serão
     * colocados no mesmo "grupo" (lista). A função retorna um mapa onde as
     * chaves são os valores retornados por `f` e os valores associados são os
     * lista com os elementos para os quais `f` retornou aquela chave.
     *
     * @param <S> tipo de dados retornado por `f`
     * @param f função de discriminação dos elementos
     * @return um mapa com os grupos formados pela função `f`
     */
    public <S> Map<S, Seq<T>> groupBy(Function<T, S> f) {
        Seq<T> nil = empty();
        HashMap<S, Seq<T>> m = new HashMap<>();
        this.forEach(x -> {
            S key = f.apply(x);
            m.put(key, cons(x, m.getOrDefault(key, nil)));
        });
        return m;
    }

    /**
     * Realiza uma partição sobre esta lista, i.e., dado um predicado `p`, duas
     * lista são formadas, uma para os elementos que satisfazem a `p` e outra
     * com os elementos para os quais `p` falha.
     *
     * @param p predicados usado para gerar a partição
     * @return um par ordenado com duas listas, a primeira com o elementos que
     * satisfazem `p`, e a segunda com os elementos em que `p` falha
     */
    public Pair<Seq<T>, Seq<T>> partition(Predicate<T> p) {
        Builder<T> b1 = new Builder<>();
        Builder<T> b2 = new Builder<>();
        this.forEach(x -> {
            if (p.test(x)) {
                b1.add(x);
            } else {
                b2.add(x);
            }
        });
        return p(b1.build(), b2.build());
    }

    /**
     * Gera uma string representando esta lista. A string inicia com `ldelim`,
     * em seguida todos os elementos da lista são inserido na string, separados
     * por `sep`, e a string termina com `rdelim`.
     *
     * @param ldelim delimitador de início da string
     * @param rdelim delimitador de final da string
     * @param sep separador entre os elementos da lista
     * @return uma string representando esta lista
     */
    public String mkStr(String ldelim, String rdelim, String sep) {
        StringBuilder sb = new StringBuilder();
        sb.append(ldelim);
        if (!this.isEmpty()) {
            sb.append(this.head());
            this.tail().forEach(x -> sb.append(sep).append(String.valueOf(x)));
        }
        sb.append(rdelim);
        return sb.toString();
    }

    /**
     * Gera uma string representando esta lista. A string inicia com "[", em
     * seguida todos os elementos da lista são inserido na string, separados por
     * ", ", e a string termina com "rdelim".
     *
     * @return uma string representando esta lista
     */
    public String mkStr() {
        return mkStr("[", "]", ", ");
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "Nil";
        } else {
            return this.mkStr("Seq{", "}", ", ");
        }
    }

    /**
     * Retorna uma lista padrão de Java contendo os mesmos elementos que esta
     * lista.
     *
     * @return esta lista convertida para a tipo de Lista padrão de Java
     */
    public List<T> toList() {
        List<T> l = new ArrayList<>();
        this.forEach(x -> l.add(x));
        return l;
    }

    /**
     * Retorna um iterador sobre os elementos desta lista.
     *
     * @return um iterador sobre os elementos desta lista
     */
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private Seq<T> curr = Seq.this;

            @Override
            public boolean hasNext() {
                return curr.isEmpty();
            }

            @Override
            public T next() {
                T nxt = curr.head();
                curr = curr.tail();
                return nxt;
            }

        };
    }

    /**
     * Retorna uma stream padrão de Java contendo os mesmos elementos que esta
     * lista.
     *
     * @return esta lista convertida para uma stream de Java
     */
    public Stream<T> toStream() {
        Iterator<T> it = iterator();
        Spliterator<T> split = Spliterators.spliteratorUnknownSize(it, Spliterator.DISTINCT);
        Stream<T> s = StreamSupport.stream(split, false);
        return s;
    }

    /**
     * Classe que representa lista vazias.
     *
     * @param <T> tipo de dados dos elementos da lista
     */
    protected static class Nil<T> extends Seq<T> {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public T head() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Seq<T> tail() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int hashCode() {
            int hash = 23;
            hash = 53 * hash + Objects.hashCode(this.getClass());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }

    }

    /**
     * Classe que representa os blocos de construção da lista, i.e., um par com
     * dado e o "apontador" para o resto da lista.
     *
     * @param <T> tipo de dado dos elementos da lista
     */
    protected static class Cons<T> extends Seq<T> {

        private T head;
        private Seq<T> tail;

        public Cons(T head, Seq<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Seq<T> tail() {
            return tail;
        }

        private void setTail(Seq<T> tail) {
            this.tail = tail;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.head);
            hash = 47 * hash + Objects.hashCode(this.tail);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Cons<?> other = (Cons<?>) obj;
            if (!Objects.equals(this.head, other.head)) {
                return false;
            }
            if (!Objects.equals(this.tail, other.tail)) {
                return false;
            }
            return true;
        }

    }

    /**
     * Um construtor mutável para listas encadeadas. Os objetos desta classe são
     * usados internamente pelos métodos da classe `Seq` para contrução de
     * listas. Esta classe encapsula todas as operações mutáveis sobre listas
     * encadedeadas.
     *
     * @param <S> tipo de dados dos elementos da lista
     */
    public static class Builder<S> {

        private final Seq<S> nil;
        private Seq<S> acc;
        private Cons<S> last;
        private boolean done;

        public Builder() {
            this.nil = Seq.empty();
            this.acc = this.nil;
            this.last = null;
            this.done = false;
        }

        /**
         * Adiciona um elemento ao construtor da lista. Gera um erro caso seja
         * chamado após o método `build`.
         *
         * @param x elementos que será adicionado ao construtor
         * @return o construtor com seu estado alterado com mais um elemento
         */
        public Builder<S> add(S x) {
            if (done) {
                throw new IllegalStateException("Builder is done!");
            }
            if (acc.isEmpty()) {
                acc = cons(x, nil);
                last = (Cons<S>) acc;
            } else {
                last.setTail(cons(x, nil));
                last = (Cons<S>) last.tail;
            }
            return this;
        }

        /**
         * Adiciona todos os elementos da lista `s2` ao construtor da lista.
         * Gera um erro caso seja chamado após o método `build`.
         *
         * @param s2 lista cujos elementos serão adicionados ao construtor
         * @return o construtor com seu estado alterado com os elementos de `s2`
         */
        public Builder<S> addAll(Seq<S> s2) {
            if (done) {
                throw new IllegalStateException("Builder is done!");
            }
            Seq<S> it = s2;
            while (!it.isEmpty()) {
                add(it.head());
                it = it.tail();
            }
            return this;
        }

        /**
         * Retorna a lista que foi construída até este momento. Após chamar este
         * método, não será mais possível adicionar elementos ao construtor.
         *
         * @return a lista que foi construída
         */
        public Seq<S> build() {
            this.done = true;
            return acc;
        }

    }

}
