package demo.reservation.service.jobtype;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.repository.JobTypeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class JobTypeServiceImpl implements JobTypeService {
    @Inject
    JobTypeRepository jobTypeRepository;

    /**
     * Method to find {@link JobType} by name and type
     * @param query represents the job type query
     * @param jobTypeEnum represents the job type
     */
    @Override
    public Optional<JobType> find(String query, JobTypeEnum jobTypeEnum) {
        return jobTypeRepository.find(query, jobTypeEnum.name()).stream().findFirst();
    }
}
