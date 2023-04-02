package demo.reservation.service.mechanics;

import demo.reservation.persistence.domain.Mechanics;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.Optional;

public interface MechanicsService {
    Optional<Mechanics> find(String query, String mechanicsName);
    PanacheQuery<Mechanics> findAll();
}
