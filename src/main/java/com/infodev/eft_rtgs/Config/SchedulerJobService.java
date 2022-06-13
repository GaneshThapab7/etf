package com.infodev.eft_rtgs.Config;

import com.infodev.eft_rtgs.Model.Qurtz.SchedulerJobInfo;
import com.infodev.eft_rtgs.component.JobScheduleCreator;
import com.infodev.eft_rtgs.enms.CronStatus;
import com.infodev.eft_rtgs.job.Transaction_CronJOb;
import com.infodev.eft_rtgs.view.SchedulerRepository;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Service
@Configuration
@EnableScheduling
public class SchedulerJobService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private SchedulerRepository schedulerRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private JobScheduleCreator scheduleCreator;

	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}

	public List<SchedulerJobInfo> getAllJobList() {
		List<SchedulerJobInfo> result = new ArrayList<SchedulerJobInfo>();
	schedulerRepository.findAll().forEach(e->{
		result.add(e);
	});
		return result;
	}

    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
        	SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        	schedulerRepository.delete(getJobInfo);
        	log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
        	SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        	getJobInfo.setJobStatus("PAUSED");
        	schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " paused.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
        	SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        	getJobInfo.setJobStatus("RESUMED");
        	schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " resumed.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
        	SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled and started now.");
        	getJobInfo.setJobStatus("SCHEDULED & STARTED");
        	schedulerRepository.save(getJobInfo);

            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled and started now.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

	@SuppressWarnings("deprecation")
	public void saveOrupdate(SchedulerJobInfo scheduleJob) throws Exception {
		/*if (scheduleJob.getCronExpression().length() > 0) {
			scheduleJob.setJobClass(Transaction_CronJOb.class.getName());
			scheduleJob.setCronJob(true);
		} else {
			scheduleJob.setJobClass(Transaction_CronJOb.class.getName());
			scheduleJob.setCronJob(false);
			scheduleJob.setRepeatTime((long) 1);
		}*/
		scheduleJob.setJobClass(Transaction_CronJOb.class.getName());
		scheduleJob.setCronJob(true);
		if (StringUtils.isEmpty(scheduleJob.getJobId())) {
			log.info("Job Info: {}", scheduleJob);
			scheduleNewJob(scheduleJob);
		} else {
			updateScheduleJob(scheduleJob);
		}
		scheduleJob.setDesc("Transaction Crom job number " + scheduleJob.getJobId());
		scheduleJob.setInterfaceName("interface_" + scheduleJob.getJobId());
		log.info(">>>>> jobName = [" + scheduleJob.getJobName() + "]" + " created.");
	}

	@SuppressWarnings("unchecked")
	private void scheduleNewJob(SchedulerJobInfo jobInfo) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			JobDetail jobDetail = JobBuilder
					.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {

				jobDetail = scheduleCreator.createJob(
						(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
						jobInfo.getJobName(), jobInfo.getJobGroup());

				Trigger trigger;
				if (jobInfo.getCronJob()) {
					trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				} else {
					trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				}
				scheduler.scheduleJob(jobDetail, trigger);
				jobInfo.setJobStatus("SCHEDULED");
				schedulerRepository.save(jobInfo);
				log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
			} else {
				log.error("scheduleNewJobRequest.jobAlreadyExist");
			}
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void updateScheduleJob(SchedulerJobInfo jobInfo) {
		Trigger newTrigger;
		if (jobInfo.getCronJob()) {
			newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
					jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		} else {
			newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		}
		try {
			schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
			jobInfo.setJobStatus("EDITED & SCHEDULED");
			schedulerRepository.save(jobInfo);
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}


	//@Scheduled(cron = "0/10 * * * * ?")
	public void reportCurrentTime() {
	//	schedulerRepository.deleteById(Long.valueOf(30) );
	SchedulerJobInfo schedulerJobInfo=schedulerRepository.findFirstByCronStatus(CronStatus.READY.getKey());

	if(schedulerJobInfo!=null){
		log.info(" Cron Ready To execute", new Date()+"  Cron ID: "+schedulerJobInfo.getJobId());

		startJobNow(schedulerJobInfo);
	}else {
		log.info("No Cron Ready To execute", new Date());
	}
	}


	public String newCronexpressionset(Long cronDelay){
		LocalDateTime now = LocalDateTime.now().plusMinutes(cronDelay);


        return ("0 "+now.getMinute()+" "+now.getHour()+" "+now.getDayOfMonth()+" "+now.getMonth().getValue()+ " ? "+now.getYear());
	}
	public String Cronexpressionsetmonth(Long cronDelay){
		LocalDateTime now = LocalDateTime.now().plusMonths(cronDelay);


		return ("0 "+now.getMinute()+" "+now.getHour()+" "+now.getDayOfMonth()+" "+now.getMonth().getValue()+ " ? "+now.getYear());
	}
}
