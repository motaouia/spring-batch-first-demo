package org.medmota.demobatch;

import javax.sql.DataSource;

import org.medmota.demobatch.batchutils.BatchJobListener;
import org.medmota.demobatch.batchutils.BatchStepSkipper;
import org.medmota.demobatch.dto.ConvertedInputData;
import org.medmota.demobatch.dto.InputData;
import org.medmota.demobatch.processor.BatchProcessor;
import org.medmota.demobatch.reader.BatchReader;
import org.medmota.demobatch.writer.BatchWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
@EnableBatchProcessing
public class DemoSpringBatchDeveloppezApplication {

	@Value("${path.to.the.work.dir}")
	private String workDirPath;

	@Autowired
	private DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBatchDeveloppezApplication.class, args);
	}

	@Bean
	public JobRepository jobRepositoryObj() throws Exception {
		JobRepositoryFactoryBean jobRepoFactory = new JobRepositoryFactoryBean();
		jobRepoFactory.setTransactionManager(transactionManager);
		jobRepoFactory.setDataSource(dataSource);
		return jobRepoFactory.getObject();
	}

	@Bean
	public JobRepository jobRepositoryW() throws Exception {
		JobRepositoryFactoryBean jobRepoFactory = new JobRepositoryFactoryBean();
		jobRepoFactory.setTransactionManager(transactionManager);
		jobRepoFactory.setDataSource(dataSource);
		return jobRepoFactory.getObject();
	}

	@Bean
	public BatchReader batchReader() {
		return new BatchReader(workDirPath);
	}

	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}

	@Bean
	public BatchWriter batchWriter() {
		return new BatchWriter();
	}

	@Bean
	public BatchJobListener batchJobListener() {
		return new BatchJobListener();
	}

	@Bean
	public BatchStepSkipper batchStepSkipper() {
		return new BatchStepSkipper();
	}

	@Bean
	public Step batchStep() {
		return stepBuilderFactory.get("stepDatawarehouseLoader").transactionManager(transactionManager)
				.<InputData, ConvertedInputData>chunk(1).reader(batchReader()).processor(batchProcessor())
				.writer(batchWriter()).faultTolerant().skipPolicy(batchStepSkipper()).build();
	}

	@Bean
	@Scheduled(cron = "*/2 * * * * *")
	public Job jobStep() throws Exception {
		return jobBuilderFactory.get("jobDatawarehouseLoader").repository(jobRepositoryW())
				.incrementer(new RunIdIncrementer()).listener(batchJobListener()).flow(batchStep()).end().build();
	}

}
