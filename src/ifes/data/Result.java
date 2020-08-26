package ifes.data;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Resultado de uma computação que pode obter sucesso ou falhar. Semelhante ao
 * tipo {@code Optional<?>} do Java, exceto pelo fato de que em caso de falha é
 * retornada uma mensagem de erro.
 *
 * @author Jefferson Andrade
 * @param <T> tipo de dados do valor resultante da computação
 */
public abstract class Result<T> {

    public static <S> Result<S> success(S value) {
        return new Success<>(value);
    }

    public static <S> Result<S> failure(String message) {
        return new Failure<>(message);
    }

    public static <S,T> Result<S> failure(Failure<T> orig) {
        return new Failure<>(orig.getMessage());
    }

    public static <S> Result<S> failure() {
        return new Failure<>("Undefined failure.");
    }

    protected Result() {}

    public boolean isSuccess() {
        return false;
    }

    public boolean isFailure() {
        return false;
    }

    public T get() {
        throw new UnsupportedOperationException();
    }
    
    public abstract T getOrElse(T x);

    public abstract T getOrElse(Supplier<T> f);

    public String getMessage() {
        throw new UnsupportedOperationException();
    }
    
    public abstract <S> Result<S> map(Function<T, S> f);

    public abstract <S> Result<S> flatMap(Function<T, Result<S>> f);

    /**
     * Valor obtido quando uma computação tem sucesso.
     * 
     * @param <T> tipo de dados resultante da computação
     */
    protected static class Success<T> extends Result<T> {

        private final T value;

        protected Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T getOrElse(T x) {
            return value;
        }

        @Override
        public T getOrElse(Supplier<T> f) {
            return value;
        }

        @Override
        public <S> Result<S> map(Function<T, S> f) {
            S y = f.apply(value);
            return success(y);
        }

        @Override
        public <S> Result<S> flatMap(Function<T, Result<S>> f) {
            Result<S> r = f.apply(value);
            return r;
        }

    }

    /**
     * Valor obtido quando uma computação falha.
     * 
     * @param <T> 
     */
    protected static class Failure<T> extends Result<T> {

        private final String message;

        protected Failure(String message) {
            this.message = message;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public String getMessage() {
            return message;
        }


        @Override
        public T getOrElse(T x) {
            return x;
        }

        @Override
        public T getOrElse(Supplier<T> f) {
            T x = f.get();
            return x;
        }

        @Override
        public <S> Result<S> map(Function<T, S> f) {
            return failure(this);
        }

        @Override
        public <S> Result<S> flatMap(Function<T, Result<S>> f) {
            return failure(this);
        }

    }
    
}
