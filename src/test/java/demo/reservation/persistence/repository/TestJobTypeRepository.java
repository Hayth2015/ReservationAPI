package demo.reservation.persistence.repository;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.persistence.domain.JobType;
import io.quarkus.arc.Priority;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.transaction.Transactional;

@Priority(1)
@Alternative
@ApplicationScoped
@Transactional
public class TestJobTypeRepository extends JobTypeRepository {
    @PostConstruct
    public void init() {
        persist(new JobType(JobTypeEnum.TEST, 5));
    }
}
