package com.demo.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.demo.batch.models.Persona;

@Component
public class JobListener extends JobExecutionListenerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);
	private final JdbcTemplate jdbcTemplate;

	public JobListener(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOG.info("Job finalizado");
			jdbcTemplate
					.query("SELECT  primer_nombre, segundo_nombre, telefono FROM personas",
							(rs, row) -> new Persona(rs.getString(1), rs.getString(2), rs.getString(3)))
					.forEach(p -> LOG.info("Registro : " + p));

		}
	}
}
