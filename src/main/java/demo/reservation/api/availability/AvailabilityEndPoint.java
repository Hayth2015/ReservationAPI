package demo.reservation.api.availability;

import demo.reservation.api.model.dto.AvailabilityRequestDTO;
import demo.reservation.service.availability.AvailabilityService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class AvailabilityEndPoint implements AvailabilityAPI {

    @Inject
    AvailabilityService availabilityService;

    /**
     *
     * @param availabilityRequestDTO represents the set of parameters to use for the search
     * @return Response object that contains the availability list
     */
    @Override
    public Response getAvailabilitiesForJobTypeAndOrMechanics(AvailabilityRequestDTO availabilityRequestDTO) {
        return availabilityService.getAvailabilitiesForJobTypeAndOrMechanics(availabilityRequestDTO);
    }
}
