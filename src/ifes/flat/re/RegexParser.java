package ifes.flat.re;

import java.util.Objects;

/**
 *
 * @author jefferson
 */
public class RegexParser {

    public final String input;
    private int index;

    public RegexParser(String input) {
        this.input = input;
        this.index = 0;
    }

    /**
     * Sintaxe em EBNF:
     * <regexp> ::= <union>
     * <union> ::= <concat> {'|' <concat>}
     * <concat> ::= <star> {<star>}
     * <star> ::= <atom> ['*']
     * <atom> ::= <nothing> | <empty> | <char> | '(' <regexp> ')'
     * <nothing> ::= '\0'
     * <empty> ::= '\e'
     * <char> ::= '\\' | '\(' | '\)' | '\*' | '\|' | "caracteres..."
     *
     * @return
     */
    public Regex<Character> parse() {
        index = 0;
        return regexp();
    }

    private Regex<Character> regexp() {
        return union();
    }

    private Regex<Character> union() {
        var r1 = concat();
        while (match("|")) {
            index += 1;
            var r2 = concat();
            r1 = r1.union(r2);
        }
        return r1;
    }

    private Regex<Character> concat() {
        var r1 = star();
        if (r1 != null) {
            var r2 = star();
            while (r2 != null) {
                r1 = r1.concat(r2);
                r2 = star();
            }
        }
        return r1;
    }

    private Regex<Character> star() {
        var r1 = atom();
        if (r1 != null && match("*")) {
            r1 = r1.star();
            index += 1;
        }
        return r1;
    }

    private Regex<Character> atom() {
        Regex<Character> re = null;
        if (match("\\")) {
            index += 1;
            if (match("0")) {
                re = new Nothing<>();
                index += 1;
            } else if (match("e")) {
                re = new Empty<>();
                index += 1;
            } else if (match("\\")) {
                re = new Literal('\\');
                index += 1;
            } else if (match("(")) {
                re = new Literal('(');
                index += 1;
            } else if (match(")")) {
                re = new Literal(')');
                index += 1;
            } else if (match(")")) {
                re = new Literal(')');
                index += 1;
            } else if (match("*")) {
                re = new Literal('*');
                index += 2;
            } else if (match("|")) {
                re = new Literal('|');
                index += 1;
            } else {
                re = null;
            }
        } else if (match("(")) {
            index += 1;
            re = regexp();
            expect(")");
        } else if (match(")") || match("*") || match("|")) {
            re = null;
        } else if (index < input.length()) {
            Character c = input.charAt(index);
            re = new Literal(c);
            index += 1;
        } else {
            //throw new IndexOutOfBoundsException(index);
            re = null;
        }
        return re;
    }

    private boolean match(String text) {
        return index < input.length()
                && input.substring(index).startsWith(text);
    }

    private void expect(String delim) {
        if (input.substring(index).startsWith(delim)) {
            index += delim.length();
        } else {
            String u = input.substring(index, index + delim.length());
            String msg = String.format("Invalid input at %d. Expecing: %s; found: %s.", index, delim, u);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public String toString() {
        return "RegexParser{" + "input=" + input + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.input);
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
        final RegexParser other = (RegexParser) obj;
        if (!Objects.equals(this.input, other.input)) {
            return false;
        }
        return true;
    }

}
