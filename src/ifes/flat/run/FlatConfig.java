package ifes.flat.run;

import ifes.cli.Config;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Objects;

/**
 *
 * @author jefferson
 */
public class FlatConfig extends Config {

    public Boolean debug;
    public Boolean showHelp;
    public Boolean showVersion;
    public BufferedReader reader;
    public PrintWriter writer;
    public String command;
    
    public FlatConfig(Boolean debug, Boolean showUsage, 
            Boolean showVersion, BufferedReader reader, 
            PrintWriter writer, String command) {
        this.debug = debug;
        this.showHelp = showUsage;
        this.showVersion = showVersion;
        this.reader = reader;
        this.writer = writer;
        this.command = command;
    }
    
    public FlatConfig(FlatConfig c) {
        this(c.debug, c.showHelp, c.showVersion, c.reader, c.writer, c.command);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.debug);
        hash = 59 * hash + Objects.hashCode(this.showHelp);
        hash = 59 * hash + Objects.hashCode(this.showVersion);
        hash = 59 * hash + Objects.hashCode(this.reader);
        hash = 59 * hash + Objects.hashCode(this.writer);
        hash = 59 * hash + Objects.hashCode(this.command);
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
        final FlatConfig other = (FlatConfig) obj;
        if (!Objects.equals(this.command, other.command)) {
            return false;
        }
        if (!Objects.equals(this.debug, other.debug)) {
            return false;
        }
        if (!Objects.equals(this.showHelp, other.showHelp)) {
            return false;
        }
        if (!Objects.equals(this.showVersion, other.showVersion)) {
            return false;
        }
        if (!Objects.equals(this.reader, other.reader)) {
            return false;
        }
        if (!Objects.equals(this.writer, other.writer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FlatConfig{" 
                + "debug=" + debug + ", showHelp=" + showHelp 
                + ", showVersion=" + showVersion + ", reader=" + reader 
                + ", writer=" + writer + ", command=" + command + '}';
    }
    
}
