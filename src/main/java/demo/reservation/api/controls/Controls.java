package demo.reservation.api.controls;

import demo.reservation.persistence.domain.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Controls {

    private Controls() {
        //Private constructor to prevent this Static class from instantiation
    }

    /**
     * Method to check whether given appointment date and time are valid parameters or not, in comparison with current date
     * @param appointmentRequestDatetime the {@link LocalDateTime} appointment date time in request
     * @param jobTypeDef the {@link JobType} job type definition in database
     * @return true when the provided parameters are valid date and time in comparison with current date
     */
    public static boolean validateAppointmentDate(LocalDateTime appointmentRequestDatetime, JobType jobTypeDef) {
        return !appointmentRequestDatetime.isBefore(LocalDateTime.now().plusMinutes(jobTypeDef.getJobDuration()));
    }

    /**
     * Verifies if the provided date is before th current date
     * @param requestDate represents the requested date
     * @return true if the provided date is after the current date time
     */
    public static boolean validateRequestDate(LocalDate requestDate) {
        return !requestDate.isBefore(LocalDate.now());
    }
}
