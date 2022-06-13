package com.infodev.eft_rtgs.Model.Qurtz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@ToString
@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    @Column(name = "job_id")
    private String jobId;
    @Basic
    @Column(name = "job_name")
    private String jobName;
    @Basic
    @Column(name = "job_group")
    private String jobGroup;
    @Basic
    @Column(name = "job_status")
    private String jobStatus;
    @Basic
    @Column(name = "job_class")
    private String jobClass;
    @Basic
    @Column(name = "cron_expression")
    private String cronExpression;
    @Basic
    @Column(name = "desc")
    private String desc;
    @Basic
    @Column(name = "interface_name")
    private String interfaceName;
    @Basic
    @Column(name = "repeat_time")
    private Long repeatTime;
    @Basic
    @Column(name = "cron_job")
    private Boolean cronJob;
    @Basic
    @Column(name = "cron_status")
    private int cronStatus;

}
