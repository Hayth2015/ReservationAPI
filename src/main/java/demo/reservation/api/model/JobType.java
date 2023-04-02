package demo.reservation.api.model;
import demo.reservation.api.model.dto.JobTypeEnum;

public class JobType {

    private JobTypeEnum jobType;
    private int jobDuration;

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
