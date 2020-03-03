package com.magnarox.batch.manager.batch;

import com.magnarox.batch.manager.entities.TutoPeople;
import com.magnarox.batch.manager.repositories.TutoPeopleRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public FlatFileItemReader<TutoPeople> reader(@Value("#{stepExecutionContext[fileName]}") String filename) {
        return new FlatFileItemReaderBuilder<TutoPeople>()
                .name("personReader")
                .resource(new ClassPathResource("input/" + filename))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TutoPeople>() {{
                    setTargetType(TutoPeople.class);
                }})
                .build();
    }

    @Bean
    public ResourcePartitioner partitioner() throws IOException {
        return new ResourcePartitioner().setResources(getResources());
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    @StepScope
    public JpaRepositoryItemWriter<TutoPeople> writer(final TutoPeopleRepository repository) {
        return new JpaRepositoryItemWriter<>(repository);
    }

    @Bean
    public Job partitionerJob(final BatchJobExecutionListener listener, final Step partitionedStep) {
        return jobBuilderFactory.get("partitionerJob")
                .listener(listener)
                .start(partitionedStep)
                .build();
    }

    @Bean
    public Step partitionedStep(final JpaRepositoryItemWriter<TutoPeople> writer) throws IOException {
        return stepBuilderFactory.get("partitionStep")
                .partitioner("slaveStep", partitioner())
                .step(slaveStep(writer))
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep(final JpaRepositoryItemWriter<TutoPeople> writer) {
        return stepBuilderFactory.get("slaveStep")
                .<TutoPeople, TutoPeople>chunk(1)
                .reader(reader(null))
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    private Resource[] getResources() throws IOException {
        final File inputDir = new ClassPathResource("input").getFile();
        if (!inputDir.exists() || !inputDir.isDirectory())
            throw new IOException("Bad input configuration");

        return Arrays.stream(inputDir.list()).map(x -> "input/" + x).map(ClassPathResource::new).toArray(ClassPathResource[]::new);
    }
}
