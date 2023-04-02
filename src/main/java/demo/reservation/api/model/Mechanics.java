package demo.reservation.api.model;

import demo.reservation.persistence.domain.Appointment;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

public class Mechanics {

    private String name;
    private String function;

    public Mechanics(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public Mechanics() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
