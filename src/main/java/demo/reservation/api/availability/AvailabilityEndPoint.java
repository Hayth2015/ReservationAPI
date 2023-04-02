package demo.reservation.api.availability;

import demo.reservation.api.model.Availability;
import demo.reservation.api.model.dto.AvailabilityRequestDTO;
import demo.reservation.service.availability.AvailabilityService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class AvailabilityEndPoint implements AvailabilityAPI {

    @Inject
    AvailabilityService availabilityService;

    /**
     * Returns list of {@link Availability} for the given parameters
     * @param availabilityRequestDTO represents the set of parameters to use for the search
     * @return list of {@link Availability}
     */
    @Override
    public Response getAvailabilitiesForJobTypeAndOrMechanics(AvailabilityRequestDTO availabilityRequestDTO) {
        return availabilityService.getAvailabilitiesForJobTypeAndOrMechanics(availabilityRequestDTO);
    }
}
