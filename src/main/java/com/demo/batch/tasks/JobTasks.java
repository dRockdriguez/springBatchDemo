package com.demo.batch.tasks;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTasks {
	private final JobLauncher launcher;
	private final Job job;
	private static final Logger LOG = LoggerFactory.getLogger(JobTasks.class);

	@Autowired
	public JobTasks(final JobLauncher launcher, final Job job) {
		this.launcher = launcher;
		this.job = job;
	}

	@Scheduled(fixedRate = 10000)
	public void executeJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		LOG.info("Inicio :: " + new Date());
		JobParameters parameters = new JobParametersBuilder()
				.addString("JobId", String.valueOf(System.currentTimeMillis())).toJobParameters();

		launcher.run(job, parameters);
	}
}
