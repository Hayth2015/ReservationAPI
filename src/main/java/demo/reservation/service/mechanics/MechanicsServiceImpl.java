package demo.reservation.service.mechanics;

import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import demo.reservation.persistence.repository.MechanicsRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class MechanicsServiceImpl implements MechanicsService {
    @Inject
    MechanicsRepository mechanicsRepository;

    /**
     * Method to find {@link JobType} by name and type
     * @param query represents the job type query
     * @param mechanicsName represents the mechanics name
     */
    @Override
    public Optional<Mechanics> find(String query, String mechanicsName) {
        return mechanicsRepository.find(query, mechanicsName).stream().findFirst();
    }

    public PanacheQuery<Mechanics> findAll() {
        return mechanicsRepository.findAll();
    }
}
