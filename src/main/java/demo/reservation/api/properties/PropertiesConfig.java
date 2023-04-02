package demo.reservation.api.properties;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "property")
public interface PropertiesConfig {

    @WithName("message.resource.available")
    @WithDefault("Resources not available")
    String message();

    @WithName("mechanicsA.working.days")
    @WithDefault("1,3,4,6")
    String mechanicsAWorkingDays();

    @WithName("mechanicsB.working.days")
    @WithDefault("1,2,3,4,5")
    String mechanicsBWorkingDays();

    @WithName("garage.working.days")
    @WithDefault("1,2,3,4,5,6")
    String garageWorkingDays();

    @WithName("garage.opening.time")
    @WithDefault("8")
    String garageOpeningTime();

    @WithName("garage.break.time")
    @WithDefault("12")
    String breakTime();

    @WithName("garage.closing.time")
    @WithDefault("17")
    String closingTime();
}
