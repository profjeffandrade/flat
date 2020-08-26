package ifes.cli;

import ifes.data.Result;

/**
 *
 * @author jefferson
 */
public interface Validator<T> {
    
    public Result<T> test(T value);
    
}
