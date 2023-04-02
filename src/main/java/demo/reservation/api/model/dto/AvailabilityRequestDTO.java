package demo.reservation.api.model.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "AvailabilityRequest", description = "POJO that represents a request for available timeslots")
public record AvailabilityRequestDTO (@Schema(required = true) LocalDate fromDate,
                                      @Schema(required = true) LocalDate toDate,
                                      @Schema() JobTypeEnum jobTypeEnum,
                                      @Schema() String mechanics) {
}
