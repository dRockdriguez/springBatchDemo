package com.demo.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.demo.batch.listener.JobListener;
import com.demo.batch.models.Persona;
import com.demo.batch.processor.PersonaItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public BatchConfig(final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public FlatFileItemReader<Persona> reader() {
		return new FlatFileItemReaderBuilder<Persona>().name("personaItemReader")
				.resource(new ClassPathResource("data.csv")).delimited()
				.names(new String[] { "primerNombre", "segundoNombre", "telefono" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>() {
					{
						setTargetType(Persona.class);
					}
				}).build();
	}

	@Bean
	public PersonaItemProcessor processor() {
		return new PersonaItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Persona> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Persona>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO personas (primer_nombre, segundo_nombre, telefono) VALUES (:primerNombre, :segundoNombre, :telefono)")
				.dataSource(dataSource).build();
	}

	@Bean
	public Job importPersonaJob(JobListener listener, Step step1) {
		return jobBuilderFactory.get("importPersonaJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1).end().build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Persona> writer) {
		return stepBuilderFactory.get("step1").<Persona, Persona>chunk(10).reader(reader()).processor(processor())
				.writer(writer).build();
	}
}
