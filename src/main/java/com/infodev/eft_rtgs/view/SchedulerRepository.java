package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.Qurtz.SchedulerJobInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends CrudRepository<SchedulerJobInfo, Long> {

	SchedulerJobInfo findByJobName(String jobName);

	void deleteById(Long id);


	SchedulerJobInfo findFirstByCronStatus(int status);

	SchedulerJobInfo findFirstByCronStatusOrderByCronStatusDesc(int staus);

	SchedulerJobInfo findFirstByCronStatusOrderByCronStatusAsc(int cron_status);


}
