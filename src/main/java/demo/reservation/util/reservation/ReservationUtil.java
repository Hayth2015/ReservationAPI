package demo.reservation.util.reservation;

import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public final class ReservationUtil {

    private ReservationUtil() {
        throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
    }
    static Logger logger = LoggerFactory.getLogger(ReservationUtil.class);

    public static Optional<TimeSet> retrieveAvailableTimeslots(LocalDate requestedDate, JobType jobType, List<Appointment> scheduledAppointments) {

        //Convert scheduled appointments into TimeSlots
        List<TimeSlot> scheduledTimeSlots = new ArrayList<>(scheduledAppointments
                .stream()
                .map(appointment -> TimeSlot.of(appointment.getAppointmentTime(), appointment.getAppointmentTime().plusMinutes(appointment.getJobType().getJobDuration())))
                .toList());

        //Add break to the scheduled time slots
        LocalDateTime breakDayTime = LocalDateTime.of(requestedDate, LocalTime.of(12,0,0));
        TimeSlot breakTimeSlot = TimeSlot.of(breakDayTime, 30, ChronoUnit.MINUTES);
        scheduledTimeSlots.add(breakTimeSlot);

        //Define a timeslot for the garage working day
        LocalDateTime garageStartDayTime = LocalDateTime.of(requestedDate, LocalTime.of(9,0,0));
        LocalDateTime garageEndDayTime = LocalDateTime.of(requestedDate, LocalTime.of(17, 30,0));
        TimeSlot dayTimeSlot = TimeSlot.of(garageStartDayTime, garageEndDayTime);

        //Find the available timeslots, by comparing the garage day timeslot and the reserves timeslots for the same day
        List<TimeSlot> availableTimeSlotsForTheDay = dayTimeSlot.subtract(scheduledTimeSlots);

        //Compare the available time slots with the requested job type duration
        //and return the possible timeslots
        List<TimeSlot> availableTimeSlots =  availableTimeSlotsForTheDay
                .stream()
                .filter(timeSlot -> timeSlot.length(ChronoUnit.MINUTES) >= jobType.getJobDuration())
                .toList();

        return !availableTimeSlots.isEmpty() ? Optional.of(TimeSet.of(availableTimeSlots)) : Optional.empty();
    }

    /**
     * Method to verify if the given appointment request is in an available timeslot
     * @param appointmentDateTime {@link LocalDateTime} represents the appointment date and time
     * @param jobType {@link JobType} represents the job type requested
     * @param scheduledAppointments represents list of scheduled appointments
     * @return true if a time slot is available
     */
    public static boolean checkAvailableTimeslots(LocalDateTime appointmentDateTime, JobType jobType, List<Appointment> scheduledAppointments) {
        TimeSlot requestedTimeSlot = TimeSlot.of(appointmentDateTime, jobType.getJobDuration(), ChronoUnit.MINUTES);

        //Convert scheduled appointments into TimeSlots
        List<TimeSlot> scheduledTimeSlots = new ArrayList<>(scheduledAppointments
                .stream()
                .map(appointment -> TimeSlot.of(appointment.getAppointmentTime(), appointment.getAppointmentTime().plusMinutes(appointment.getJobType().getJobDuration())))
                .toList());

        //Add break to the scheduled time slots
        LocalDateTime breakDayTime = LocalDateTime.of(appointmentDateTime.toLocalDate(), LocalTime.of(12,0,0));
        TimeSlot breakTimeSlot = TimeSlot.of(breakDayTime, 30, ChronoUnit.MINUTES);

        scheduledTimeSlots.add(breakTimeSlot);

        //Define a timeslot for the day
        LocalDateTime garageStartDayTime = LocalDateTime.of(appointmentDateTime.toLocalDate(), LocalTime.of(9,0,0));
        LocalDateTime garageEndDayTime = LocalDateTime.of(appointmentDateTime.toLocalDate(), LocalTime.of(17, 30,0));
        TimeSlot dayTimeSlot = TimeSlot.of(garageStartDayTime, garageEndDayTime);

        //Find the available timeslots, by comparing the garage day timeslot and the reserves timeslots for the same day
        List<TimeSlot> availableTimeSlotsForTheDay = dayTimeSlot.subtract(scheduledTimeSlots);

        //Compare the available time slots with the requested timeslot
        //and return true if possible timeslots exist
        return availableTimeSlotsForTheDay
                .stream()
                .anyMatch(timeslot -> timeslot.contains(requestedTimeSlot));
    }

    public static boolean validateMechanicsAvailability(String mechanicsName, LocalDateTime appointmentDatetime, String mechanicsWorkingDays) {
        return validateMechanicsAvailability(mechanicsName, appointmentDatetime.toLocalDate(), mechanicsWorkingDays);
    }

    /**
     * Method to validate if the provided mechanics name exists in database
     * and verifies if the provided appointment day is within the working days of the garage and the mechanics
     * @param mechanicsName the {@link String} mechanics name
     * @param appointmentDate the {@link LocalDate} appointment date
     * @return the {@link Mechanics} if validated, otherwise returns null
     */
    public static boolean validateMechanicsAvailability(String mechanicsName, LocalDate appointmentDate, String mechanicsWorkingDays) {
        final int appointmentDayOfTheWeek = appointmentDate.getDayOfWeek().getValue();
        //Retrieve and validate garage working days from configuration
        return validateMechanicsWorkingDay(mechanicsName, appointmentDayOfTheWeek, mechanicsWorkingDays);
    }

    /**
     * Method to validate if the appointment is within the garage working days
     *
     * @param garageWorkingDays represents the garage working days as configured in configuration proeprties
     * @param appointmentDayOfTheWeek represents the appointment day of the week
     * @return true if the appointment is in the working days of the garage
     */
    public static boolean validateGarageWorkingDays(String garageWorkingDays, int appointmentDayOfTheWeek) {
        final List<Integer> garageDaysAvailability = Stream.of(garageWorkingDays.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
        return garageDaysAvailability.contains(appointmentDayOfTheWeek);
    }

    /**
     * Method to validate if the appointment is within the mechanics working days
     * @param mechanicsName represents the mechanics name
     * @param appointmentDayOfTheWeek represents the appointment day of the week
     * @return true if the appointment is in the working days of the garage
     */
    private static boolean validateMechanicsWorkingDay(String mechanicsName, int appointmentDayOfTheWeek, String mechanicsWorkingDays) {
        if(StringUtils.isEmpty(mechanicsWorkingDays)) {
            logger.debug("Missing resource configuration for Mechanics {}", mechanicsName);
            return false;
        }
        final List<Integer> mechanicsDaysAvailability =  Stream.of(mechanicsWorkingDays.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        return mechanicsDaysAvailability.contains(appointmentDayOfTheWeek);
    }
}
