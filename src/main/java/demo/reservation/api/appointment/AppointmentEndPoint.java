package demo.reservation.api.appointment;

import demo.reservation.api.model.dto.AppointmentRequestDTO;
import demo.reservation.service.appointment.AppointmentService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

public class AppointmentEndPoint implements AppointmentAPI {

    @Inject
    AppointmentService appointmentService;

    /**
     * Creates a new appointment
     * @param mechanicsName {@link String} the provided mechanics
     * @param appointmentRequestDTO {@link AppointmentRequestDTO} the request data that includes time and job type
     * @return {@link Response} with code 201 when success
     */
    @Override
    @Transactional
    public final Response createAppointmentForMechanics(String mechanicsName, AppointmentRequestDTO appointmentRequestDTO) {
        return appointmentService.createAppointmentForMechanics(mechanicsName, appointmentRequestDTO);
    }

    @Override
    public Response getAllAppointments() {
        return Response
                .status(Response.Status.NOT_IMPLEMENTED)
                .entity("Not yet implemented")
                .build();
    }

    @Override
    public Response getAllAppointmentsForMechanics(String mechanicsId) {
        return Response
                .status(Response.Status.NOT_IMPLEMENTED)
                .entity("Not yet implemented")
                .build();
    }
}
