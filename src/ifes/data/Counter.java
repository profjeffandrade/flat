package ifes.data;

import java.util.Objects;

/**
 * Um contador com operações de incremente, decremento e reset.
 * 
 * @author Jefferson Andrade
 */
public class Counter implements Comparable<Counter> {

    private Integer value;
    private final Integer initialValue;
    
    public Counter(Integer value) {
        this.initialValue = value;
        this.value = value;
    }
    
    public Counter() {
        this(0);
    }
    
    public Integer get() {
        return this.value;
    }
    
    public void set(Integer value) {
        this.value = value;
    }
    
    public void reset() {
        this.value = this.initialValue;
    }
    
    
    public void inc() {
        this.value += 1;
    }
    
    public void inc(int d) {
        this.value += d;
    }
    
    public void dec() {
        this.value -= 1;
    }
    
    public void dec(int d) {
        this.value -= d;
    }

    @Override
    public int compareTo(Counter t) {
        Counter other = (Counter) t;
        return value.compareTo(other.value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.value);
        hash = 11 * hash + Objects.hashCode(this.initialValue);
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
        final Counter other = (Counter) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.initialValue, other.initialValue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Counter{" + "value=" + value + ", initialValue=" + initialValue + '}';
    }
    
}
