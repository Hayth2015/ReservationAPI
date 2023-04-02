package demo.reservation.persistence.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MECHANICS")
public class Mechanics extends PanacheEntity {

    private String name;
    private String function;

    @OneToMany(mappedBy="mechanics")
    private Set<Appointment> appointments;

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
