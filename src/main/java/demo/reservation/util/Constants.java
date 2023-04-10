package demo.reservation.util;

public final class Constants {

    /**
     * Private constructor to prevent this utility from instantiation
     */
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
    }

    public static final class EndPoints {
        private EndPoints() {
            throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
        }

        //End point for root test
        public static final String END_POINT_ROOT = "/root";


        //End points for Appointment API
        public static final String APPOINTMENT_ROOT = "/appointment";
        //Retrieves all appointment
        public static final String APPOINTMENT_ALL = "/all";
        //Retrieves all appointments for a given mechanics
        public static final String APPOINTMENT_MECHANICS_ALL = "/all/{mechanicsId}";
        //Creates an appointment for a given mechanics
        public static final String APPOINTMENT_CREATE = "/create/{mechanicsName}";


        //End points for Availability API
        public static final String AVAILABILITY_ROOT = "/availability";
        //Provides the availability (all time slots) for all job types and mechanics
        public static final String AVAILABILITY_ALL = "/all";
        //Provide the availability (all time slots) for a given job type
        public static final String AVAILABILITY_JOB_TYPE = "/all/{jobType}";
        //Provides the availability (all time slots) for a given mechanics
        public static final String AVAILABILITY_MECHANICS = "/all/{mechanicsId}";
        //Provides the availability (all time slots) for a given job type and mechanics
        public static final String AVAILABILITY_JOB_TYPE_MECHANICS = "/all/{jobType}/{mechanicsId}";
        public static final String AVAILABILITY_JOB_TYPE_AND_OR_MECHANICS = "/retrieve";

        //end point for system version
        public static final String SYSTEM_VERSION = "/version";
    }

    public static final class Mechanics {
        private Mechanics() {
            throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
        }

        public static final String MECHANICS_A = "mechanicsA";
        public static final String MECHANICS_B = "mechanicsB";
    }

    public static final class ErrorMessage {
        private ErrorMessage() {
            throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
        }

        //API response error messages
        public static final String GARAGE_NOT_AVAILABLE = "Garage not available";
        public static final String MECHANICS_NOT_AVAILABLE = "Mechanics not available";
        public static final String DATES_PROVIDED_ARE_NOT_CORRECT = "Dates provided are not correct";
        public static final String DATE_TIME_PROVIDED_IS_IN_THE_PAST = "Date time provided is in the past";

    }
}
