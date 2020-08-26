/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.flat;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jefferson
 */
public class Grammar<V, T> {
    
    public final Set<V> nonTerminals;
    public final Set<T> terminals;
    public final List<Rule> rules;
    public final V startSymbol;
    
    public Grammar(Set<V> nonTerminals, 
            Set<T> terminals, 
            List<Rule> rules,
            V startSymbol)
    {
        this.nonTerminals = Collections.unmodifiableSet(nonTerminals);
        this.terminals = Collections.unmodifiableSet(terminals);
        this.rules = Collections.unmodifiableList(rules);
        this.startSymbol = startSymbol;
        
        if (!nonTerminals.contains(startSymbol)) {
            throw new IllegalArgumentException("The start symbols is not a non terminal.");
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.nonTerminals);
        hash = 47 * hash + Objects.hashCode(this.terminals);
        hash = 47 * hash + Objects.hashCode(this.rules);
        hash = 47 * hash + Objects.hashCode(this.startSymbol);
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
        final Grammar<?, ?> other = (Grammar<?, ?>) obj;
        if (!Objects.equals(this.nonTerminals, other.nonTerminals)) {
            return false;
        }
        if (!Objects.equals(this.terminals, other.terminals)) {
            return false;
        }
        if (!Objects.equals(this.rules, other.rules)) {
            return false;
        }
        if (!Objects.equals(this.startSymbol, other.startSymbol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Grammar{" + "nonTerminals=" + nonTerminals + ", terminals=" + terminals + ", rules=" + rules + ", startSymbol=" + startSymbol + '}';
    }    
    
}
