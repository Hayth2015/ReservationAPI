package demo.reservation.persistence.repository;

import demo.reservation.persistence.domain.Mechanics;
import io.quarkus.arc.Priority;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.transaction.Transactional;

@Priority(1)
@Alternative
@ApplicationScoped
@Transactional
public class TestMechanicsRepository extends MechanicsRepository {
    @PostConstruct
    public void init() {
        persist(new Mechanics("Test mechanics", "Test function"));
    }
}
