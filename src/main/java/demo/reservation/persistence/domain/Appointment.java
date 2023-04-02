package demo.reservation.persistence.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "MECHANICS_ID", "JOBTYPE_ID", "APPOINTMENTTIME" }) })
public class Appointment extends PanacheEntity {

    @ManyToOne
    @JoinColumn(nullable=false)
    private Mechanics mechanics;

    @ManyToOne
    @JoinColumn(nullable=false)
    private JobType jobType;

    private LocalDateTime appointmentTime;

    public Appointment(Mechanics mechanics, JobType jobType, LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
        this.mechanics = mechanics;
        this.jobType = jobType;
    }

    public Appointment() {}

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime localTime) {
        this.appointmentTime = localTime;
    }

    public Mechanics getMechanics() {
        return mechanics;
    }

    public void setMechanics(Mechanics mechanics) {
        this.mechanics = mechanics;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }
}
