package demo.reservation.api.version;

import demo.reservation.util.Constants;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Constants.EndPoints.SYSTEM_VERSION)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "System resource", description = "Retrieves system information")
public interface SystemAPI {
    @GET
    @Path("/")
    @Operation(summary = "Retrieves the system version")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "System version retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = String.class),
                            example = "1.0.0"
                    )
            ),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "415", description = "Unsupported media type"),
            @APIResponse(responseCode = "500", description = "Internal server error"),
            @APIResponse(responseCode = "501", description = "Not Implemented"),
            @APIResponse(responseCode = "503", description = "Service Unavailable")

    })
    Response getSystemVersion();
}
