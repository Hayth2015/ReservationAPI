package demo.reservation.api.properties;

import demo.reservation.util.Constants;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(Constants.EndPoints.END_POINT_ROOT)
@Tag(name = "Properties resource", description = "Operations related to resources")
public class PropertiesResource {

    @Inject
    PropertiesConfig propertiesConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return propertiesConfig.message();
    }
}