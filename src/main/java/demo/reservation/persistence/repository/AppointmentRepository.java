package demo.reservation.persistence.repository;

import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.Mechanics;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Repository to manage appointments
@ApplicationScoped
public class AppointmentRepository implements PanacheRepository<Appointment> {

    private static final String FIND_APPOINTMENTS_BY_DATE_QUERY =
            "SELECT DISTINCT appointment FROM Appointment appointment WHERE mechanics = ?1 AND CAST(appointmentTime AS LocalDate) = ?2";


    //Retrieves all appointments for a given mechanics and day
    public List<Appointment> getAppointmentsByMechanicsAndDay(Mechanics mechanics, LocalDate appointmentDate) {
        return find(FIND_APPOINTMENTS_BY_DATE_QUERY, mechanics, appointmentDate).list();
    }
}
