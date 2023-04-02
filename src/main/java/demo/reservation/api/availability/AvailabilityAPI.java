package demo.reservation.api.availability;

import demo.reservation.api.model.dto.AvailabilityRequestDTO;
import demo.reservation.util.Constants;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Constants.EndPoints.AVAILABILITY_ROOT)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Availability resource", description = "Operations related to availabilities")
public interface AvailabilityAPI {
    @POST
    @Path(Constants.EndPoints.AVAILABILITY_JOB_TYPE_AND_OR_MECHANICS)
    @Operation(
            summary = "Retrieves available time slots",
            description = "The service will retrieve list of time slots for the provided parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Available timeslots retrieved"
            ),
            @APIResponse(
                    responseCode = "204",
                    description = "No available timeslots found"
            ),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "404", description = "Not found"),
            @APIResponse(responseCode = "406", description = "Not acceptable"),
            @APIResponse(responseCode = "415", description = "Unsupported media type"),
            @APIResponse(responseCode = "500", description = "Internal server error"),
            @APIResponse(responseCode = "501", description = "Not Implemented"),
            @APIResponse(responseCode = "503", description = "Service Unavailable")

    })
    Response getAvailabilitiesForJobTypeAndOrMechanics(
            @RequestBody(
                    name = "Availability request body",
                    description = "Parameters for retrieving available time slots",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AvailabilityRequestDTO.class)
                    )
            ) @Valid AvailabilityRequestDTO availabilityRequestDTO
    );
}
