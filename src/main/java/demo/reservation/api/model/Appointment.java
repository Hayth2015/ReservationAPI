package demo.reservation.api.model;

import demo.reservation.persistence.domain.JobType;
import demo.reservation.persistence.domain.Mechanics;
import java.time.LocalDateTime;

public class Appointment {
    private demo.reservation.api.model.Mechanics mechanics;
    private demo.reservation.api.model.JobType jobType;
    private LocalDateTime appointmentTime;

    public Appointment(demo.reservation.api.model.Mechanics mechanics, demo.reservation.api.model.JobType jobType, LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
        this.mechanics = mechanics;
        this.jobType = jobType;
    }

    public Appointment() {}

    public demo.reservation.api.model.Mechanics getMechanics() {
        return mechanics;
    }

    public void setMechanics(demo.reservation.api.model.Mechanics mechanics) {
        this.mechanics = mechanics;
    }

    public demo.reservation.api.model.JobType getJobType() {
        return jobType;
    }

    public void setJobType(demo.reservation.api.model.JobType jobType) {
        this.jobType = jobType;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
