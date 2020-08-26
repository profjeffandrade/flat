package ifes.data;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Um conteiner para um valor algum tipo dentre duas possibilidade.Por exemplo,
 * pode ser conveniente retornar uma string ou um mapa com propriedades.Neste
 * caso, podería-se definir o valor de retorno como
 * {@code Either<String,Map<String,String>>}. Classes análocas e de mesmo nome
 * existem em Haskell e Scala, por exmeplo. Apenas um dos dois valores pode ser
 * especificado de cada vez. As instâncias são imutáveis, i.e., uma vez criado
 * o objeto seus campos não podem ser alterados.
 *
 * @author jefferson
 * @param <L> tipo de dados do valor da esquerda
 * @param <R> tipo de dados do valor da direita
 */
public final class Either<L, R> {

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(Optional.of(value), Optional.empty());
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(Optional.empty(), Optional.of(value));
    }

    public final Optional<L> left;
    public final Optional<R> right;

    private Either(Optional<L> left, Optional<R> right) {
        this.left = left;
        this.right = right;
    }

    public boolean isLeft() {
        return left.isPresent();
    }

    public boolean isRight() {
        return right.isPresent();
    }

    public <L1> Either<L1, R> mapLeft(Function<? super L, ? extends L1> f) {
        Optional<L1> left1 = left.map(f);
        return new Either<>(left1, right);
    }

    public <R1> Either<L, R1> mapRight(Function<? super R, ? extends R1> f) {
        Optional<R1> right1 = right.map(f);
        return new Either<>(left, right1);
    }

    public <L1, R1> Either<L1, R1> map(
            Function<? super L, ? extends L1> f,
            Function<? super R, ? extends R1> g) {
        Optional<L1> left1 = left.map(f);
        Optional<R1> right1 = right.map(g);
        return new Either<>(left1, right1);
    }

    public void call(
            Consumer<? super L> f,
            Consumer<? super R> g) {
        left.ifPresent(f);
        right.ifPresent(g);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.left);
        hash = 59 * hash + Objects.hashCode(this.right);
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
        final Either<?, ?> other = (Either<?, ?>) obj;
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Either{" + "left=" + left + ", right=" + right + '}';
    }

}
