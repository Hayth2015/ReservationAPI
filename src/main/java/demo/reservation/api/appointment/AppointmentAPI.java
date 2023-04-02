package demo.reservation.api.appointment;

import demo.reservation.api.model.dto.AppointmentRequestDTO;
import demo.reservation.util.Constants;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path(Constants.EndPoints.APPOINTMENT_ROOT)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Appointment resource", description = "Operations related to appointments")
public interface AppointmentAPI {

    @POST
    @Path(Constants.EndPoints.APPOINTMENT_CREATE)
    @Operation(
            summary = "Creates an appointment for a given mechanics",
            description = "The service will create an appointment for the given mechanics")
    @Parameter(
            name = "mechanicsName",
            description = "The mechanics identifier",
            schema = @Schema(type = SchemaType.STRING)
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Appointment created"
            ),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "406", description = "Not acceptable"),
            @APIResponse(responseCode = "415", description = "Unsupported media type"),
            @APIResponse(responseCode = "500", description = "Internal server error"),
            @APIResponse(responseCode = "501", description = "Not Implemented"),
            @APIResponse(responseCode = "503", description = "Service Unavailable")

    })
    Response createAppointmentForMechanics(
            @PathParam("mechanicsName") String mechanicsName,
            @RequestBody(
                    name = "Appointment request body",
                    description = "Parameters to create an appointment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentRequestDTO.class)
                    )
            ) @Valid AppointmentRequestDTO appointmentRequestDTO
    );

    @GET
    @Path(Constants.EndPoints.APPOINTMENT_ALL)
    @Operation(summary = "Retrieves all appointments")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllAppointments();

    @GET
    @Path(Constants.EndPoints.APPOINTMENT_MECHANICS_ALL)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieves all appointments for a given mechanics")
    @Parameter(
            name = "mechanicsId",
            description = "The mechanics identifier"
            //schema = Schema(type = SchemaType.STRING)
    )
    Response getAllAppointmentsForMechanics(@PathParam("mechanicsId") String mechanicsId);
}
