package demo.reservation.persistence.repository;

import demo.reservation.persistence.domain.Mechanics;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

//Repository to manage mechanics
@ApplicationScoped
public class MechanicsRepository implements PanacheRepository<Mechanics> {
}
