package demo.reservation.persistence.domain;
import demo.reservation.api.model.dto.JobTypeEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Set;

@Entity
public class JobType extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private JobTypeEnum jobType;
    private int jobDuration;

    @OneToMany(mappedBy="jobType")
    private Set<Appointment> appointments;


    public JobType(int id, JobTypeEnum jobType, int jobDuration) {
        this.id = id;
        this.jobType = jobType;
        this.jobDuration = jobDuration;
    }

    public JobType(JobTypeEnum jobType, int jobDuration) {
        this.jobType = jobType;
        this.jobDuration = jobDuration;
    }

    public JobType() {}
    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobTypeEnum) {
        this.jobType = jobTypeEnum;
    }

    public int getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(int jobDuration) {
        this.jobDuration = jobDuration;
    }
}
