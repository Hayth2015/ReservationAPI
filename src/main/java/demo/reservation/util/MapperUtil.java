package demo.reservation.util;

import demo.reservation.persistence.domain.Appointment;

public class MapperUtil {

    private MapperUtil() {
        throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
    }

    public static demo.reservation.api.model.Appointment mapResponseEntity(Appointment newAppointment) {

        demo.reservation.api.model.Appointment appointmentResponseModel = new demo.reservation.api.model.Appointment();
        appointmentResponseModel.setAppointmentTime(newAppointment.getAppointmentTime());

        demo.reservation.api.model.JobType jobType = new demo.reservation.api.model.JobType();
        jobType.setJobType(newAppointment.getJobType().getJobType());
        jobType.setJobDuration(newAppointment.getJobType().getJobDuration());

        appointmentResponseModel.setJobType(jobType);

        demo.reservation.api.model.Mechanics mechanics = new demo.reservation.api.model.Mechanics();
        mechanics.setName(newAppointment.getMechanics().getName());
        mechanics.setFunction(newAppointment.getMechanics().getFunction());

        appointmentResponseModel.setMechanics(mechanics);

        return appointmentResponseModel;
    }
}
