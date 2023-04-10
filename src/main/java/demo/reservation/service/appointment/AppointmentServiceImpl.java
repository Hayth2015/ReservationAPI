package demo.reservation.service.appointment;

import demo.reservation.api.controls.Controls;
import demo.reservation.api.model.dto.AppointmentRequestDTO;
import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.api.properties.PropertiesConfig;
import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import demo.reservation.persistence.repository.AppointmentRepository;
import demo.reservation.service.jobtype.JobTypeService;
import demo.reservation.service.mechanics.MechanicsService;
import demo.reservation.util.Constants;
import demo.reservation.util.MapperUtil;
import demo.reservation.util.reservation.ReservationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AppointmentServiceImpl implements AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    MechanicsService mechanicsService;

    @Inject
    JobTypeService jobTypeService;

    @Inject
    PropertiesConfig propertiesConfig;

    /**
     * Creates a new appointment
     * @param mechanicsName {@link String} the provided mechanics
     * @param appointmentRequestDTO {@link AppointmentRequestDTO} the request data that includes time and job type
     * @return {@link Response} with code 201 when success
     */
    @Override
    public Response createAppointmentForMechanics(String mechanicsName, AppointmentRequestDTO appointmentRequestDTO) {
        LocalDateTime appointmentDatetime = appointmentRequestDTO.appointmentDate();
        JobTypeEnum jobTypeEnum = appointmentRequestDTO.jobType();

        Optional<Mechanics> mechanics = mechanicsService.find("NAME", mechanicsName);

        if(mechanics.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Mechanics not available")
                    .build();
        }

        //Check mechanics working day
        final String mechanicsWorkingDays = switch(mechanicsName) {
            case Constants.Mechanics.MECHANICS_A -> propertiesConfig.mechanicsAWorkingDays();
            case Constants.Mechanics.MECHANICS_B -> propertiesConfig.mechanicsBWorkingDays();
            default -> "";
        };

        //check garage working day
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        final boolean isGarageAvailable = ReservationUtil.validateGarageWorkingDays(garageWorkingDays, appointmentDatetime.getDayOfWeek().getValue());
        if(!isGarageAvailable) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.GARAGE_NOT_AVAILABLE)
                    .build();
        }

        final boolean isMechanicsAvailable = ReservationUtil.validateMechanicsAvailability(mechanicsName, appointmentDatetime, mechanicsWorkingDays);
        if(!isMechanicsAvailable) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.MECHANICS_NOT_AVAILABLE)
                    .build();
        }

        //Validate job type from the database
        final JobType jobType = validateJobType(jobTypeEnum);
        if(jobType == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Job type not found")
                    .build();
        }

        //Validate requested appointment dates
        final boolean isRequestDateValid = Controls.validateAppointmentDate(appointmentDatetime, jobType);
        if(!isRequestDateValid) {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Date provided is not valid")
                    .build();
        }

        //Retrieve list of scheduled appointments
        List<Appointment> scheduledAppointments = getAppointmentsByMechanicsAndDateTime(mechanics.get(), appointmentDatetime);

        //Retrieve available time slots
        final boolean isTimeslotAvailable = ReservationUtil.checkAvailableTimeslots(appointmentDatetime, jobType, scheduledAppointments);
        if(!isTimeslotAvailable) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("No time slot available for the given arguments")
                    .build();
        }

        final Appointment newAppointment = new Appointment(mechanics.get(), jobType, appointmentDatetime);

        appointmentRepository.persist(newAppointment);

        return Response
                .status(Response.Status.CREATED)
                .entity(MapperUtil.mapResponseEntity(newAppointment))
                .build();
    }

    /**
     * Method to validate if the provided job type exists in database
     * @param jobTypeEnum the {@link Enum} job type
     * @return {@link JobType}
     */
    public JobType validateJobType(JobTypeEnum jobTypeEnum) {
        final Optional<JobType> jobType = jobTypeService.find("JOBTYPE", jobTypeEnum);
        return jobType.orElse(null);
    }

    public List<Appointment> getAppointmentsByMechanicsAndDateTime(Mechanics mechanics, LocalDateTime appointmentDatetime) {
        return getAppointmentsByMechanicsAndDate(mechanics, appointmentDatetime.toLocalDate());
    }

    /**
     * Method to retrieve appointments by mechanics and day
     * @param mechanics represents a {@link Constants.Mechanics}
     * @param appointmentDate represents an appointment date
     * @return a list of {@link Appointment} when exists in database
     */
    public List<Appointment> getAppointmentsByMechanicsAndDate(Mechanics mechanics, LocalDate appointmentDate) {
        return appointmentRepository.getAppointmentsByMechanicsAndDay(mechanics, appointmentDate);
    }
}
