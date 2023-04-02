package demo.reservation.persistence.repository;

import demo.reservation.persistence.domain.Appointment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

//Repository to search for mechanics availability
//based on specific dates
//or all mechanics availability
@ApplicationScoped
public class AvailabilityRepository implements PanacheRepository<Appointment> {
}
