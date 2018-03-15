package be.mielnoelanders.bazinga.domain;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Parameter extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // FIELDS // valid type to be found in ParameterEnum
    private ParameterEnum type;
    private int percentage;

    // CONSTRUCTORS
    public Parameter() {
    }

    public Parameter(ParameterEnum type, int percentage) {
        this.type = type;
        this.percentage = percentage;
    }

    //GETTERS & SETTERS
    public ParameterEnum getType() {
        return type;
    }
    public void setType(ParameterEnum type) {
        this.type = type;
    }
    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    //OVERRIDES
    @Override
    public String toString() {
        return "Parameters{" +
                "id=" + super.getId() +
                ", type='" + type + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}