package com.san.pro;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;

import com.san.pro.model.User;
import com.san.pro.processor.UserDataProcessor;
import com.san.pro.util.UserRowMapper;

@Configuration
@EnableBatchProcessing
public class BatchJobConfiguration extends DefaultBatchConfigurer {

	private final Logger log = LoggerFactory.getLogger(BatchJobConfiguration.class);
	private static final SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SimpleJobLauncher jobLauncher;
	
	@Autowired
	public DataSource dataSource;
	
	@Value("${user.file.location}")
	private String fileLocation;

	@Value("${user.file.name}")
	private String fileName;
	
	@Bean
	@StepScope
	public JdbcCursorItemReader<User> reader() {
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT * FROM users_job.dbo.users");
		reader.setRowMapper(new UserRowMapper());
		return reader;
	}
	
	@Bean
	public UserDataProcessor processor() {
		return new UserDataProcessor();
	}
	
	@Bean
	@StepScope
	public FlatFileItemWriter<User> writer() {
		FlatFileItemWriter<User> writer = new FlatFileItemWriter<User>();
		String file = fileLocation + "/" + fileName + "_" + timeStamp.format(new Date()) + ".csv";
		writer.setResource(new FileSystemResource(new File(file)));
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("name | dob | contact");
			}
		});
		writer.setLineAggregator(new PassThroughLineAggregator<User>());
		return writer;
	}
	
	@Bean
	public Step step1() {
		log.debug("call from step1 method");
		return stepBuilderFactory.get("step1").<User, User> chunk(10)
				.reader(reader()).processor(processor()).writer(writer()).build();
	}
	
	@Bean
	public Job exportUserJob() {
		log.debug("call from exportUserJob method");
		return jobBuilderFactory.get("exportUserJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}
	
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}
	
	@Scheduled(fixedRate = 2000)
	//	@Scheduled(cron = "${user.schedule.cron}")
	public void perform() throws Exception {
		JobParameters param = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(exportUserJob(), param);
		log.info("Job Started at: " + execution.getCreateTime());
		log.info("Data generated at: " + execution.getEndTime());
		log.info("Job finished with status :" + execution.getStatus());

		if (execution.getStatus().toString().equals("COMPLETED")) {
			log.info("SUCCESS");
			System.out.println("SUCCESS");
		}
		
	}
	
	
}
