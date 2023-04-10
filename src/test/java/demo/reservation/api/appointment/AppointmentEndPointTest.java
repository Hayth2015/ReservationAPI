package demo.reservation.api.appointment;

import demo.reservation.api.model.dto.AppointmentRequestDTO;
import demo.reservation.api.model.dto.JobTypeEnum;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentEndPointTest {

    public static final String MECHANICS_A = "MechanicsA";
    public static final String MECHANICS_B = "MechanicsB";

    @Test
    void should_Fail_Tuesday_Not_Working_Day_Mechanics_A() {
        //Mechanics A has days off on Tuesdays and Fridays
        //18/04/2023 corresponds to a Tuesday
        LocalDate appointmentRequestDate = LocalDate.of(2023, 4, 18);
        LocalTime appointmentRequestTime = LocalTime.of(9, 0, 0);
        LocalDateTime appointmentRequestDateTime = LocalDateTime.of(appointmentRequestDate, appointmentRequestTime);

        AppointmentRequestDTO appointmentRequestDTO
                = new AppointmentRequestDTO(appointmentRequestDateTime, JobTypeEnum.LAMP_CHANGE);

        var response = createAppointmentCall(appointmentRequestDTO, MECHANICS_A);

        assertEquals(response.statusCode(), Response.Status.NOT_FOUND.getStatusCode());
    }

    private io.restassured.response.Response createAppointmentCall(AppointmentRequestDTO appointmentRequestDTO, String mechanics) {
        return given()
                .contentType("application/json")
                .body(appointmentRequestDTO)
                .when()
                .post("/create/" + mechanics)
                .then()
                .extract()
                .response();
    }

    @Test
    void should_Fail_Friday_Not_Working_Day_Mechanics_A() {
        //Mechanics A has days off on Tuesdays and Fridays
        //21/04/2023 corresponds to a Friday
        LocalDate appointmentRequestDate = LocalDate.of(2023, 4, 21);
        LocalTime appointmentRequestTime = LocalTime.of(9, 0, 0);
        LocalDateTime appointmentRequestDateTime = LocalDateTime.of(appointmentRequestDate, appointmentRequestTime);

        AppointmentRequestDTO appointmentRequestDTO
                = new AppointmentRequestDTO(appointmentRequestDateTime, JobTypeEnum.LAMP_CHANGE);

        var response = createAppointmentCall(appointmentRequestDTO, MECHANICS_A);

        assertEquals(response.statusCode(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void should_Fail_Saturday_Not_Working_Day_Mechanics_B() {
        //Mechanics A has day off on Saturday and Fridays
        //22/04/2023 corresponds to a Saturday
        LocalDate appointmentRequestDate = LocalDate.of(2023, 4, 22);
        LocalTime appointmentRequestTime = LocalTime.of(9, 0, 0);
        LocalDateTime appointmentRequestDateTime = LocalDateTime.of(appointmentRequestDate, appointmentRequestTime);

        AppointmentRequestDTO appointmentRequestDTO
                = new AppointmentRequestDTO(appointmentRequestDateTime, JobTypeEnum.LAMP_CHANGE);

        var response = createAppointmentCall(appointmentRequestDTO, MECHANICS_B);

        assertEquals(response.statusCode(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void should_Fail_Sunday_Not_Working_Day() {
        //Garage is closed on Sundays
        //23/04/2023 corresponds to a Sunday
        LocalDate appointmentRequestDate = LocalDate.of(2023, 4, 23);
        LocalTime appointmentRequestTime = LocalTime.of(9, 0, 0);
        LocalDateTime appointmentRequestDateTime = LocalDateTime.of(appointmentRequestDate, appointmentRequestTime);

        AppointmentRequestDTO appointmentRequestDTO
                = new AppointmentRequestDTO(appointmentRequestDateTime, JobTypeEnum.LAMP_CHANGE);

        var response = createAppointmentCall(appointmentRequestDTO, MECHANICS_B);

        assertEquals(response.statusCode(), Response.Status.NOT_FOUND.getStatusCode());
    }
}