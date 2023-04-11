package demo.reservation.api.properties;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class PropertiesResourceTest {

    public static final String GARAGE_WORKING_DAYS = "1,2,3,4,5,6";
    public static final String MECHANICS_A_WORKING_DAYS = "1,3,4,6";
    public static final String MECHANICS_B_WORKING_DAYS = "1,2,3,4,5";
    @Inject
    PropertiesConfig propertiesConfig;

    @Test
    void should_retrieve_garage_working_day() {
        //check garage working day
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        assertEquals(GARAGE_WORKING_DAYS, garageWorkingDays);
    }

    @Test
    void should_not_include_sundays_in_working_days() {
        //check garage working day
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        assertFalse(garageWorkingDays.contains("7"));

    }

    @Test
    void should_validate_mechanicsA_working_days() {
        //check mechanics A working day
        String mechanicsAWorkingDays = propertiesConfig.mechanicsAWorkingDays();
        assertEquals(MECHANICS_A_WORKING_DAYS, mechanicsAWorkingDays);
    }

    @Test
    void should_validate_mechanicsB_working_days() {
        //check mechanics A working day
        String mechanicsBWorkingDays = propertiesConfig.mechanicsBWorkingDays();
        assertEquals(MECHANICS_B_WORKING_DAYS, mechanicsBWorkingDays);
    }
}
