package demo.reservation.util;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperUtilTest {

    @Test
    void test_mapper() {
        Appointment appointment = createAppointmentDomainObject();

        demo.reservation.api.model.Appointment appointmentModel = MapperUtil.mapResponseEntity(appointment);

        assertEquals(appointment.getAppointmentTime(), appointmentModel.getAppointmentTime());
        assertEquals(appointment.getMechanics().getName(), appointmentModel.getMechanics().getName());
        assertEquals(appointment.getMechanics().getFunction(), appointmentModel.getMechanics().getFunction());
        assertEquals(appointment.getJobType().getJobDuration(), appointmentModel.getJobType().getJobDuration());
        assertEquals(appointment.getJobType().getJobType(), appointmentModel.getJobType().getJobType());
    }

    private Appointment createAppointmentDomainObject() {
        Mechanics mechanics = new Mechanics();
        mechanics.setName("Test name");
        mechanics.setFunction("Test function");

        JobType jobType = new JobType();
        jobType.setJobType(JobTypeEnum.TEST);
        jobType.setJobDuration(30);

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setMechanics(mechanics);
        appointment.setJobType(jobType);

        return appointment;
    }
}
