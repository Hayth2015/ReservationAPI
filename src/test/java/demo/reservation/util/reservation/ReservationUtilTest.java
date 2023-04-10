package demo.reservation.util.reservation;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.api.properties.PropertiesConfig;
import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ReservationUtilTest {

    @Inject
    PropertiesConfig propertiesConfig;

    @Test
    void should_retrieve_2_available_timeslots_for_a_general_check() {
        LocalDate requestedDateForAvailableTimeSlots = LocalDate.of(2023, 4, 17);
        JobType generalCheck = new JobType();
        generalCheck.setJobType(JobTypeEnum.GENERAL_CHECK);
        generalCheck.setJobDuration(180);
        List<Appointment> scheduledAppointments = new ArrayList<>();

        Optional<TimeSet> timeSet1 = ReservationUtil.retrieveAvailableTimeslots(requestedDateForAvailableTimeSlots, generalCheck, scheduledAppointments);
        assertEquals(2, timeSet1.map(set -> set.getTimeSlots().size()).orElse(0));
    }

    @Test
    void should_find_available_timeslot_for_general_check() {
        LocalDate requestedDateForAvailableTimeSlots = LocalDate.of(2023, 4, 17);
        LocalDateTime requestedDateTimeForAvailableTimeSlots = LocalDateTime.of(requestedDateForAvailableTimeSlots, LocalTime.of(13, 0, 0));
        JobType generalCheck = new JobType();
        generalCheck.setJobType(JobTypeEnum.GENERAL_CHECK);
        generalCheck.setJobDuration(180);
        List<Appointment> scheduledAppointments = new ArrayList<>();

        boolean isTimeslotAvailable = ReservationUtil.checkAvailableTimeslots(requestedDateTimeForAvailableTimeSlots, generalCheck, scheduledAppointments);
        assertTrue(isTimeslotAvailable);
    }

    @Test
    void should_retrieve_1_available_timeslots_for_a_general_check() {
        LocalDate requestedDateForAvailableTimeSlots = LocalDate.of(2023, 4, 17);
        JobType generalCheck = new JobType();
        generalCheck.setJobType(JobTypeEnum.GENERAL_CHECK);
        generalCheck.setJobDuration(180);
        List<Appointment> scheduledAppointments = new ArrayList<>();

        LocalDateTime existingAppointmentDateTime = LocalDateTime.of(requestedDateForAvailableTimeSlots, LocalTime.of(9, 0, 0));
        Mechanics mechanics = new Mechanics("MechanicsA", "Function");
        Appointment existingAppointment = new Appointment(mechanics, generalCheck, existingAppointmentDateTime);
        scheduledAppointments.add(existingAppointment);

        Optional<TimeSet> timeSet = ReservationUtil.retrieveAvailableTimeslots(requestedDateForAvailableTimeSlots, generalCheck, scheduledAppointments);
        assertEquals(1, timeSet.map(set -> set.getTimeSlots().size()).orElse(0));
    }

    @Test
    void should_Not_find_available_timeslot_for_general_check() {
        LocalDate requestedDateForAvailableTimeSlots = LocalDate.of(2023, 4, 17);
        LocalDateTime requestedDateTimeForAvailableTimeSlots = LocalDateTime.of(requestedDateForAvailableTimeSlots, LocalTime.of(13, 0, 0));
        JobType generalCheck = new JobType();
        generalCheck.setJobType(JobTypeEnum.GENERAL_CHECK);
        generalCheck.setJobDuration(180);
        List<Appointment> scheduledAppointments = new ArrayList<>();

        LocalDateTime existingAppointmentDateTime1 = LocalDateTime.of(requestedDateForAvailableTimeSlots, LocalTime.of(9, 0, 0));
        Mechanics mechanics = new Mechanics("MechanicsA", "Function");
        Appointment existingAppointment1 = new Appointment(mechanics, generalCheck, existingAppointmentDateTime1);
        scheduledAppointments.add(existingAppointment1);

        LocalDateTime existingAppointmentDateTime2 = LocalDateTime.of(requestedDateForAvailableTimeSlots, LocalTime.of(13, 0, 0));
        Appointment existingAppointment2 = new Appointment(mechanics, generalCheck, existingAppointmentDateTime2);
        scheduledAppointments.add(existingAppointment2);

        boolean isTimeslotAvailable = ReservationUtil.checkAvailableTimeslots(requestedDateTimeForAvailableTimeSlots, generalCheck, scheduledAppointments);
        assertFalse(isTimeslotAvailable);
    }

    @Test
    void mechanicsA_should_be_available_on_date_time() {
        String mechanicsName = "mechanicsA";
        LocalDate requestedDate = LocalDate.of(2023, 4, 17);
        LocalDateTime appointmentDatetime = LocalDateTime.of(requestedDate, LocalTime.of(8, 0, 0));
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertTrue(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDatetime, mechanicsWorkingDays));
    }

    @Test
    void mechanicsA_should_not_be_available_on_date_time() {
        String mechanicsName = "mechanicsA";
        LocalDate requestedDate = LocalDate.of(2023, 4, 18);
        LocalDateTime appointmentDatetime = LocalDateTime.of(requestedDate, LocalTime.of(8, 0, 0));
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertFalse(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDatetime, mechanicsWorkingDays));
    }

    @Test
    void mechanicsB_should_be_available_on_date_time() {
        String mechanicsName = "mechanicsB";
        LocalDate requestedDate = LocalDate.of(2023, 4, 17);
        LocalDateTime appointmentDatetime = LocalDateTime.of(requestedDate, LocalTime.of(8, 0, 0));
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertTrue(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDatetime, mechanicsWorkingDays));
    }

    @Test
    void mechanicsB_should_not_be_available_on_date_time() {
        String mechanicsName = "mechanicsB";
        LocalDate requestedDate = LocalDate.of(2023, 4, 22);
        LocalDateTime appointmentDatetime = LocalDateTime.of(requestedDate, LocalTime.of(8, 0, 0));
        String mechanicsWorkingDays = propertiesConfig.mechanicsBWorkingDays();

        assertFalse(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDatetime, mechanicsWorkingDays));
    }

    @Test
    void mechanicsA_should_be_available_on_date() {
        String mechanicsName = "mechanicsA";
        LocalDate appointmentDate = LocalDate.of(2023, 4, 17);
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertTrue(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDate, mechanicsWorkingDays));
    }

    @Test
    void mechanicsA_should_not_be_available_on_date() {
        String mechanicsName = "mechanicsA";
        LocalDate appointmentDate = LocalDate.of(2023, 4, 18);
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertFalse(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDate, mechanicsWorkingDays));
    }

    @Test
    void mechanicsB_should_be_available_on_date() {
        String mechanicsName = "mechanicsB";
        LocalDate appointmentDate = LocalDate.of(2023, 4, 17);
        String mechanicsWorkingDays = propertiesConfig.mechanicsAWorkingDays();

        assertTrue(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDate, mechanicsWorkingDays));
    }

    @Test
    void mechanicsB_should_not_be_available_on_date() {
        String mechanicsName = "mechanicsB";
        LocalDate appointmentDate = LocalDate.of(2023, 4, 22);
        String mechanicsWorkingDays = propertiesConfig.mechanicsBWorkingDays();

        assertFalse(ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDate, mechanicsWorkingDays));
    }

    @Test
    void garage_should_be_available() {
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 1));
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 2));
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 3));
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 4));
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 5));
        assertTrue(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 6));
        assertFalse(ReservationUtil.validateGarageWorkingDays(garageWorkingDays, 7));
    }
}