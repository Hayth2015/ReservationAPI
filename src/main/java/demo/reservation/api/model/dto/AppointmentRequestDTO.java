package demo.reservation.api.model.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "AppointmentRequest", description = "POJO that represents an appointment request")
public record AppointmentRequestDTO(@Schema(required = true) LocalDateTime appointmentDate,
                                    @Schema(required = true) JobTypeEnum jobType) {
}
