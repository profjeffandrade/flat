package ifes.data;



/**
 *
 * @author jefferson
 */
public class BooleanBuilder {

    private Boolean value;
    
    public BooleanBuilder(Boolean value) {
        this.value = value;
    }
    
    public Boolean get() {
        return value;
    }
    
    public void set(Boolean value) {
        this.value = value;
    }
    
    public Boolean and(Boolean value) {
        this.value = this.value && value;
        return this.value;
    }
    
    public Boolean or(Boolean value) {
        this.value = this.value || value;
        return this.value;
    }
    
}
