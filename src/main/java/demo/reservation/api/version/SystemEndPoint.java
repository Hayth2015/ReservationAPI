package demo.reservation.api.version;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.ws.rs.core.Response;

public class SystemEndPoint implements SystemAPI {

    @ConfigProperty(name = "quarkus.application.version", defaultValue = "Unspecified")
    String appVersion;

    @Override
    public Response getSystemVersion() {
        return Response.status(200)
                .entity(new VersionEntity(appVersion))
                .build();
    }
}
