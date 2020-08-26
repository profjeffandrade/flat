package ifes.cli;

import static ifes.data.Result.success;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jefferson
 * @param <C>
 * @param <T>
 */
public class Opt<C extends Config, T> {

    public String typeName;
    public String shortName;
    public String longName;
    public String valueName;
    public String description;
    public boolean required;
    public boolean visible;
    public Function<String, T> convert;
    public BiFunction<C, T, C> action;
    public Validator<T> validate;

    protected Opt(
            String typeName,
            String shortName,
            String longName,
            String valueName,
            String description,
            boolean required,
            boolean visible,
            Function<String, T> convert,
            BiFunction<C, T, C> action,
            Validator<T> validate) {
        this.typeName = typeName;
        this.shortName = shortName;
        this.longName = longName;
        this.valueName = valueName;
        this.description = description;
        this.required = required;
        this.visible = visible;
        this.convert = convert;
        this.action = action;
        this.validate = validate;
    }

    protected Opt(
            String typeName,
            String shortName,
            String longName,
            String valueName,
            String description,
            boolean required,
            boolean visible) {
        this(typeName, shortName, longName, valueName, description,
                required, visible,
                (String x) -> null,
                (C cfg, T value) -> cfg,
                (T x) -> success(null)
        );
    }

    protected Opt(
            String typeName,
            String shortName,
            String longName,
            String valueName,
            String description,
            boolean required,
            boolean visible,
            Function<String, T> convert) {
        this(typeName, shortName, longName, valueName, description,
                required, visible, convert,
                (C cfg, T value) -> cfg,
                (T x) -> success(null)
        );
    }

    protected Opt(Opt<C, T> a) {
        this(a.typeName, a.shortName, a.longName, a.valueName, a.description,
                a.required, a.visible, a.convert, a.action, a.validate);
    }

    public boolean isFlag() {
        return false;
    }

    public boolean isCommand() {
        return false;
    }

    public boolean isSequence() {
        return false;
    }
    
    public boolean needValue() {
        return true;
    }

    @Override
    public String toString() {
        return "Opt<" + typeName + ">{"
                + "shortName=" + shortName
                + ", longName=" + longName
//                + ", valueName=" + valueName
//                + ", description=" + description
                + ", required=" + required
                + ", visible=" + visible
                + ", convert=" + convert
                + ", action=" + action
                + ", validate=" + validate
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.typeName);
        hash = 37 * hash + Objects.hashCode(this.shortName);
        hash = 37 * hash + Objects.hashCode(this.longName);
        hash = 37 * hash + Objects.hashCode(this.valueName);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + (this.required ? 1 : 0);
        hash = 37 * hash + (this.visible ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.convert);
        hash = 37 * hash + Objects.hashCode(this.action);
        hash = 37 * hash + Objects.hashCode(this.validate);
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
        final Opt<?, ?> other = (Opt<?, ?>) obj;
        if (this.required != other.required) {
            return false;
        }
        if (this.visible != other.visible) {
            return false;
        }
        if (!Objects.equals(this.typeName, other.typeName)) {
            return false;
        }
        if (!Objects.equals(this.shortName, other.shortName)) {
            return false;
        }
        if (!Objects.equals(this.longName, other.longName)) {
            return false;
        }
        if (!Objects.equals(this.valueName, other.valueName)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.convert, other.convert)) {
            return false;
        }
        if (!Objects.equals(this.action, other.action)) {
            return false;
        }
        if (!Objects.equals(this.validate, other.validate)) {
            return false;
        }
        return true;
    }

    public String dashShortName() {
        return '-' + shortName;        
    }
    
    public boolean matchShortName(String a) {
        return dashShortName().equals(a);
    }
    
    public String dashLongName() {
        return "--" + longName;
    }

    public boolean matchLongName(String a) {
        return a.startsWith(dashLongName() + "=");
    }

    public Optional<String> getLongNameValue(String a) {
        String re = String.format("%s=(.*)$", dashLongName());
        Matcher m = Pattern.compile(re).matcher(a);
        if (m.groupCount() < 1) {
            return Optional.empty();
        }
        else {
            return Optional.of(m.group(1));
        }
    }

}
