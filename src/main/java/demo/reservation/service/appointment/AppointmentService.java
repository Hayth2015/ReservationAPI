package demo.reservation.service.appointment;

import demo.reservation.api.model.dto.AppointmentRequestDTO;
import javax.ws.rs.core.Response;

public interface AppointmentService {

    Response createAppointmentForMechanics(String mechanicsName, AppointmentRequestDTO appointmentRequestDTO);
}
