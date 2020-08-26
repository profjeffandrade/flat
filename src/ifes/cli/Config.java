package ifes.cli;

import ifes.data.Seq;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jefferson
 */
public abstract class Config {
    
    public Seq<String> rest;
    
    public Config() {
        this.rest = Seq.empty();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.rest);
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
        final Config other = (Config) obj;
        if (!Objects.equals(this.rest, other.rest)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Config{" + "rest=" + rest + '}';
    }
    
}
