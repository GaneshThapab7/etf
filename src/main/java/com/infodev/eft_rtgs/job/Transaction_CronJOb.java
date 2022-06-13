package com.infodev.eft_rtgs.job;

import com.infodev.eft_rtgs.Config.SchedulerJobService;
import com.infodev.eft_rtgs.Controller.MQ.RabbitMQSenderInterface;
import com.infodev.eft_rtgs.Model.Qurtz.SchedulerJobInfo;
import com.infodev.eft_rtgs.enms.CronStatus;
import com.infodev.eft_rtgs.view.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Service
@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class Transaction_CronJOb extends QuartzJobBean {
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Value("${new_cron_rundelay}")
    private long cronDelay;
    @Autowired
    private SchedulerJobService schedulerJobService;

    @Autowired
    RabbitMQSenderInterface rabbitMQSenderInterface;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("SampleCronJob Start................"+context.getJobDetail());

        log.info("save my data "+context.getJobDetail());
        IntStream.range(0, 10).forEach(i -> {
            log.info("Counting - {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        log.info("SampleCronJob End................"+context.getJobDetail().getKey().getGroup());


       //update next detail expression to run in  cronDelay min
        updateanotherData( context);

    }

    private void updateanotherData(JobExecutionContext context){
       SchedulerJobInfo schedulerJobInfoold= schedulerRepository.findByJobName(context.getJobDetail().getKey().getGroup());
       //update the staus hear
        schedulerJobInfoold.setCronStatus(CronStatus.SUCCESS.getKey());
       schedulerJobService.pauseJob(schedulerJobInfoold);





        SchedulerJobInfo schedulerJobInfo= schedulerRepository.findFirstByCronStatusOrderByCronStatusAsc(CronStatus.PENDING.getKey());
        String generateregex="";
        LocalDateTime now = LocalDateTime.now();
        now.plusMinutes(cronDelay);
        if(schedulerJobInfo!=null){
            generateregex="0 "+now.getMinute()+" "+now.getHour()+" "+now.getMonth()+ " "+now.getYear();
            schedulerJobInfo.setCronExpression(generateregex);
            schedulerJobInfo.setCronStatus(CronStatus.READY.getKey());
            schedulerRepository.save(schedulerJobInfo);
        }

    }

}
