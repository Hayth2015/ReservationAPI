package demo.reservation.persistence.repository;

import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

//Repository to manage job types
@ApplicationScoped
public class JobTypeRepository implements PanacheRepository<JobType> {
}
