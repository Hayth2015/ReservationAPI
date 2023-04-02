package demo.reservation.api.model.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Enumeration that represents job types")
public enum JobTypeEnum {
    GENERAL_CHECK("General check"),
    TIRE_REPLACEMENT("Tire replacement"),
    LAMP_CHANGE("Lamp change"),
    TEST("Test operation");

    private final String operation;
    JobTypeEnum(final String operation) {
        this.operation = operation;
    }
    public String getOperation() {
        return operation;
    }

}
