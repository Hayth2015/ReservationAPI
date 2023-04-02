package demo.reservation.service.jobtype;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.persistence.domain.JobType;

import java.util.Optional;

public interface JobTypeService {
    Optional<JobType> find(String query, JobTypeEnum jobTypeEnum);
}
