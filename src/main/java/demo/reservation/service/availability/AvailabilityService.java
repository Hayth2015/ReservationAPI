package demo.reservation.service.availability;


import demo.reservation.api.model.dto.AvailabilityRequestDTO;

import javax.ws.rs.core.Response;

public interface AvailabilityService {

   Response getAvailabilitiesForJobTypeAndOrMechanics(AvailabilityRequestDTO availabilityRequestDTO);
}
