package demo.reservation.service.availability;

import demo.reservation.api.controls.Controls;
import demo.reservation.api.model.Availability;
import demo.reservation.api.model.dto.AvailabilityRequestDTO;
import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.api.properties.PropertiesConfig;
import demo.reservation.persistence.domain.Appointment;
import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import demo.reservation.persistence.repository.AppointmentRepository;
import demo.reservation.service.jobtype.JobTypeService;
import demo.reservation.service.mechanics.MechanicsService;
import demo.reservation.util.Constants;
import demo.reservation.util.reservation.ReservationUtil;
import demo.reservation.util.reservation.TimeSet;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@ApplicationScoped
public class AvailabilityServiceImpl implements AvailabilityService {

    Logger logger = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    @Inject
    MechanicsService mechanicsService;

    @Inject
    JobTypeService jobTypeService;

    @Inject
    PropertiesConfig propertiesConfig;

    @Inject
    AppointmentRepository appointmentRepository;

    /**
     * Service that returns list of {@link Availability} for a given parameters
     * @param availabilityRequestDTO represents the job type to search for
     * @return list of {@link Availability}
     */
    @Override
    public Response getAvailabilitiesForJobTypeAndOrMechanics(AvailabilityRequestDTO availabilityRequestDTO) {
        LocalDate availabilityRequestDateFrom = availabilityRequestDTO.fromDate();
        LocalDate availabilityRequestDateTo = availabilityRequestDTO.toDate();

        JobTypeEnum jobTypeEnum = availabilityRequestDTO.jobTypeEnum();
        String mechanicsName = availabilityRequestDTO.mechanics();

        //Validate time slot
        final boolean isTimeSlotValid = Controls.validateRequestDate(availabilityRequestDateTo);
        if(!isTimeSlotValid) {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity(Constants.ErrorMessage.DATE_TIME_PROVIDED_IS_IN_THE_PAST)
                    .build();
        }

        if(availabilityRequestDateFrom.isAfter(availabilityRequestDateTo)) {
            return Response
                    .status(Response.Status.NOT_IMPLEMENTED)
                    .entity(Constants.ErrorMessage.DATES_PROVIDED_ARE_NOT_CORRECT)
                    .build();
        }

        if(jobTypeEnum != null && !StringUtils.isEmpty(mechanicsName)) {
            //Search availability by job type and mechanics
            return searchAvailability(jobTypeEnum, mechanicsName, availabilityRequestDateFrom, availabilityRequestDateTo);
        } else if(jobTypeEnum != null) {
            //Search availability by job type, for all mechanics
            //grouped by day -> job type -> all mechanics
            return searchAvailability(jobTypeEnum, availabilityRequestDateFrom, availabilityRequestDateTo);
        } else if(!StringUtils.isEmpty(mechanicsName)) {
            //Search availability by mechanics, for all job types
            //grouped by day -> mechanics -> all job type
            return searchAvailability(mechanicsName, availabilityRequestDateFrom, availabilityRequestDateTo);
        } else {
            //Search availability for all mechanics and job types
            //grouped by day -> all job type -> all mechanics
            return searchAvailability(availabilityRequestDateFrom, availabilityRequestDateTo);
        }
    }

    /**
     * Search for available time slots by job type, mechanics and date range
     * @param jobTypeEnum represents the job type to search for
     * @param mechanicsName represents the mechanics to search for
     * @param availabilityRequestDateFrom represent the requested start date
     * @param availabilityRequestDateTo represent the requested end date
     * @return list of available time slots
     */
    private Response searchAvailability(JobTypeEnum jobTypeEnum,
                                        String mechanicsName,
                                        LocalDate availabilityRequestDateFrom,
                                        LocalDate availabilityRequestDateTo) {

        //Search availability by job type and mechanics, grouped by day
        if (availabilityRequestDateFrom.equals(availabilityRequestDateTo)
                && (validateWorkingDay(availabilityRequestDateFrom.getDayOfWeek().getValue()))) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Constants.ErrorMessage.GARAGE_NOT_AVAILABLE)
                        .build();
        }

        Map<String, TimeSet> timeSetsPerDay = new HashMap<>();

        //Iterate through the days and retrieve the available timeslots per job type and mechanics
        availabilityRequestDateFrom
                .datesUntil(availabilityRequestDateTo)
                .forEach(localDate -> collectAvailability(localDate, jobTypeEnum, mechanicsName, timeSetsPerDay));

        return Response
                .status(Response.Status.OK)
                .entity(timeSetsPerDay)
                .build();
    }

    /**
     * Search for available time slots by mechanics and date range
     * @param jobTypeEnum represents the job type to search for
     * @param availabilityRequestDateFrom represent the requested start date
     * @param availabilityRequestDateTo represent the requested end date
     * @return list of available time slots
     */
    private Response searchAvailability(JobTypeEnum jobTypeEnum,
                                        LocalDate availabilityRequestDateFrom,
                                        LocalDate availabilityRequestDateTo) {
        //Validate mechanics availability (if dates are in same day, it needs to be in the working days of the given mechanics)
        if (availabilityRequestDateFrom.equals(availabilityRequestDateTo)
                && validateWorkingDay(availabilityRequestDateFrom.getDayOfWeek().getValue())) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.GARAGE_NOT_AVAILABLE)
                    .build();
        }

        Map<String, Map<String, TimeSet>> timeSetsPerDay = new HashMap<>();
        //Iterate through the days and retrieve the available timeslots per mechanics
        availabilityRequestDateFrom
                .datesUntil(availabilityRequestDateTo)
                .forEach(localDate -> collectAvailability(localDate, jobTypeEnum, timeSetsPerDay));

        return Response
                .status(Response.Status.OK)
                .entity(timeSetsPerDay)
                .build();
    }

    /**
     * Search for available time slots by mechanics and date range
     * @param mechanicsName represents the mechanics to search for
     * @param availabilityRequestDateFrom represent the requested start date
     * @param availabilityRequestDateTo represent the requested end date
     * @return list of available time slots
     */
    private Response searchAvailability(String mechanicsName, LocalDate availabilityRequestDateFrom, LocalDate availabilityRequestDateTo) {
        //Validate mechanics availability (if dates are in same day, it needs to be in the working days of the given mechanics)
        if (availabilityRequestDateFrom.equals(availabilityRequestDateTo)
                && validateWorkingDay(availabilityRequestDateFrom.getDayOfWeek().getValue())) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.GARAGE_NOT_AVAILABLE)
                    .build();
        }

        //Validate if the provided mechanics name exists in database
        final Optional<Mechanics> mechanics = mechanicsService.find("NAME", mechanicsName);
        if(mechanics.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.MECHANICS_NOT_AVAILABLE)
                    .build();
        }

        Map<String, Map<String, TimeSet>> timeSetsPerDay = new HashMap<>();
        //Iterate through the days and retrieve the available timeslots per job type
        availabilityRequestDateFrom
                .datesUntil(availabilityRequestDateTo)
                .forEach(localDate -> collectAvailability(localDate, mechanicsName, timeSetsPerDay));

        return Response
                .status(Response.Status.OK)
                .entity(timeSetsPerDay)
                .build();
    }

    /**
     * Method that retrieves list of working days form the configuration properties
     * @param mechanicsName represents the mechanics name
     * @return list of working days
     */
    private String getMechanicsWorkingDays(String mechanicsName) {
        return switch(mechanicsName) {
            case Constants.Mechanics.MECHANICS_A -> propertiesConfig.mechanicsAWorkingDays();
            case Constants.Mechanics.MECHANICS_B -> propertiesConfig.mechanicsBWorkingDays();
            default -> "";
        };
    }

    /**
     * Search for all available time slots between provided dates, for all job types and all mechanics
     * @param availabilityRequestDateFrom represent the requested start date
     * @param availabilityRequestDateTo represent the requested end date
     * @return list of available time slots, grouped by day, job type then mechanics
     */
    private Response searchAvailability(LocalDate availabilityRequestDateFrom, LocalDate availabilityRequestDateTo) {

        if (availabilityRequestDateFrom.equals(availabilityRequestDateTo)
                && validateWorkingDay(availabilityRequestDateFrom.getDayOfWeek().getValue())) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Constants.ErrorMessage.GARAGE_NOT_AVAILABLE)
                    .build();
        }

        List<Map<String, Map<String, TimeSet>>> listOfAvailabilitiesByJobTypeGroupedByMechanics = new ArrayList<>();

        //Iterate through the days and retrieve the available timeslots per job type
        availabilityRequestDateFrom
                .datesUntil(availabilityRequestDateTo)
                .forEach(localDate -> collectAvailability(localDate, listOfAvailabilitiesByJobTypeGroupedByMechanics));

        return Response
                .status(Response.Status.OK)
                .entity(listOfAvailabilitiesByJobTypeGroupedByMechanics)
                .build();
    }

    /**
     * Retieves list of availabilities per day, grouped by job type and mechanics
     * @param localDate represents the day to search for
     * @param listOfAvailabilitiesByJobTypeGroupedByMechanics list that collects availabilities
     */
    private void collectAvailability(LocalDate localDate,
                                     List<Map<String, Map<String, TimeSet>>> listOfAvailabilitiesByJobTypeGroupedByMechanics) {

        //Collect availability for all job types
        Stream.of(JobTypeEnum.values())
                .forEach(jobTypeEnum -> {
                    Map<String, Map<String, TimeSet>> timeSetsPerDay = new HashMap<>();
                    collectAvailability(localDate, jobTypeEnum, timeSetsPerDay);
                    listOfAvailabilitiesByJobTypeGroupedByMechanics.add(timeSetsPerDay);
                });
    }

    /**
     * Collects availability for the given job type and mechanics
     * in form of group of time set (series of time slots) grouped by day
     * @param localDate the date to use for the search
     * @param jobTypeEnum the job type to search for
     * @param mechanicsName the mechanics to search for
     * @param timeSetPerDay the collection of availabilities in form of {@link TimeSet} grouped by day
     */
    private void collectAvailability(LocalDate localDate,
                                     JobTypeEnum jobTypeEnum,
                                     String mechanicsName,
                                     Map<String, TimeSet> timeSetPerDay) {
        final JobType jobTypeDef = validateJobType(jobTypeEnum);
        if(jobTypeDef == null) {
            return;
        }

        //Check mechanics working day
        final String mechanicsWorkingDay = getMechanicsWorkingDays(mechanicsName);
        //Check mechanics availability
        boolean isMechanicsAvailable = ReservationUtil.validateMechanicsAvailability(mechanicsName, localDate, mechanicsWorkingDay);
        if(!isMechanicsAvailable) {
            return;
        }

        //Check garage availability
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        final boolean isGarageAvailable = ReservationUtil.validateGarageWorkingDays(garageWorkingDays, localDate.getDayOfWeek().getValue());
        if(!isGarageAvailable) {
            return;
        }

        Optional<Mechanics> mechanics = mechanicsService.find("NAME", mechanicsName);
        if(mechanics.isEmpty()) {
            return;
        }

        //Retrieve list of scheduled appointments
        List<Appointment> scheduledAppointments = getAppointmentsByMechanicsAndDate(mechanics.get(), localDate);

        Optional<TimeSet> timeSetForMechanics = ReservationUtil.retrieveAvailableTimeslots(localDate, jobTypeDef, scheduledAppointments);
        timeSetForMechanics.ifPresent(timeSet -> timeSetPerDay.put(localDate.toString(), timeSet));
    }

    /**
     * Collects availability for the given job type
     * in form of group of time set (series of time slots) grouped by job type
     * then collected into a Map and grouped by day
     * @param localDate the date to use for the search
     * @param jobTypeEnum the job type to search for
     * @param timeSetsPerDay the collection of availabilities in form of {@link TimeSet} grouped by day and mechanics
     */
    private void collectAvailability(LocalDate localDate,
                                     JobTypeEnum jobTypeEnum,
                                     Map<String, Map<String, TimeSet>> timeSetsPerDay) {

        //Check garage availability
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        final boolean isGarageAvailable = ReservationUtil.validateGarageWorkingDays(garageWorkingDays, localDate.getDayOfWeek().getValue());
        if(!isGarageAvailable) {
            return;
        }

        final JobType jobTypeDef = validateJobType(jobTypeEnum);
        if(jobTypeDef == null) {
            return;
        }

        Map<String, TimeSet> timeSetsByMechanics = new HashMap<>();
        //Retrieve list of mechanics and loop through them to find availabilities for the provided job type
        PanacheQuery<Mechanics> queryList = mechanicsService.findAll();
        List<Mechanics> listOfMechanics = queryList.stream().toList();
        listOfMechanics.forEach(mechanics -> {
            //Check mechanics working day
            final String mechanicsWorkingDay = getMechanicsWorkingDays(mechanics.getName());
            //Check mechanics availability
            boolean isMechanicsAvailable = ReservationUtil.validateMechanicsAvailability(mechanics.getName(), localDate, mechanicsWorkingDay);
            if(!isMechanicsAvailable) {
                return;
            }

            //Retrieve list of scheduled appointments
            List<Appointment> scheduledAppointments = getAppointmentsByMechanicsAndDate(mechanics, localDate);
            Optional<TimeSet> timeSlots = ReservationUtil.retrieveAvailableTimeslots(localDate, jobTypeDef, scheduledAppointments);
            timeSlots.ifPresent(timeSet -> timeSetsByMechanics.put(mechanics.getName(), timeSet));
        });
        timeSetsPerDay.put(localDate.toString() + "-" + jobTypeEnum, timeSetsByMechanics);
    }

    /**
     * Collects availability for the given mechanics
     * in form of group of time set (series of time slots) grouped by job type
     * then collected into a Map and grouped by day
     * @param localDate the date to use for the search
     * @param mechanicsName the mechanics to search for
     * @param timeSetsPerDay the collection of availabilities in form of {@link TimeSet} grouped by day and job type
     */
    private void collectAvailability(LocalDate localDate,
                                     String mechanicsName,
                                     Map<String, Map<String, TimeSet>> timeSetsPerDay) {
        Map<String, TimeSet> timeSetsByJobType = new HashMap<>();

        //Check mechanics working day
        final String mechanicsWorkingDay = getMechanicsWorkingDays(mechanicsName);
        //Check mechanics availability
        boolean isMechanicsAvailable = ReservationUtil.validateMechanicsAvailability(mechanicsName, localDate, mechanicsWorkingDay);
        if(!isMechanicsAvailable) {
            return;
        }

        //Check garage availability
        String garageWorkingDays = propertiesConfig.garageWorkingDays();
        final boolean isGarageAvailable = ReservationUtil.validateGarageWorkingDays(garageWorkingDays, localDate.getDayOfWeek().getValue());
        if(!isGarageAvailable) {
            return;
        }

        Optional<Mechanics> mechanics = mechanicsService.find("NAME", mechanicsName);
        if(mechanics.isEmpty()) {
            return;
        }
        //Collect availability for all job types
        Stream.of(JobTypeEnum.values())
                .forEach(jobTypeEnum -> {
                    final JobType jobTypeDef = validateJobType(jobTypeEnum);
                    if(jobTypeDef != null) {
                        //Retrieve list of scheduled appointments
                        List<Appointment> scheduledAppointments = getAppointmentsByMechanicsAndDate(mechanics.get(), localDate);
                        Optional<TimeSet> timeSlots = ReservationUtil.retrieveAvailableTimeslots(localDate, jobTypeDef, scheduledAppointments);
                        timeSlots.ifPresent(timeSet -> timeSetsByJobType.put(jobTypeDef.getJobType().name(), timeSet));
                    } else {
                        logger.debug("Job type definition not recognised: {}", jobTypeEnum);
                    }
                });
        timeSetsPerDay.put(localDate.toString(), timeSetsByJobType);
    }

    /**
     * Method to check whether the day is within the working days of the garage
     * @param dayOfTheWeek represents the day of the week
     * @return true if the garage is working in the provided date
     */
    private boolean validateWorkingDay(int dayOfTheWeek) {
        final String garageWorkingDays = propertiesConfig.garageWorkingDays();

        return ReservationUtil.validateGarageWorkingDays(garageWorkingDays, dayOfTheWeek);
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
