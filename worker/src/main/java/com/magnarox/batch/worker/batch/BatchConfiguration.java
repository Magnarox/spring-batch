package com.magnarox.batch.worker.batch;

import com.magnarox.batch.worker.entities.TutoPeople;
import com.magnarox.batch.worker.repositories.TutoPeopleRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableBatchIntegration
public class BatchConfiguration {

    @Autowired
    private RemotePartitioningWorkerStepBuilderFactory remoteStepBuilderFactory;

    @Autowired
    private ConsumerFactory kafkaFactory;

    /*
     * Configure inbound flow (requests coming from the master)
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties("batchtopic");
        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TutoPeople> reader(@Value("#{stepExecutionContext[filePath]}") String filePath) {
        return new FlatFileItemReaderBuilder<TutoPeople>()
                .name("personReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TutoPeople>() {{
                    setTargetType(TutoPeople.class);
                }})
                .build();
    }

    @Bean
    @StepScope
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    @StepScope
    public JpaRepositoryItemWriter<TutoPeople> writer(final TutoPeopleRepository repository) {
        return new JpaRepositoryItemWriter<>(repository);
    }

    @Bean
    public Step workerStep(final JpaRepositoryItemWriter<TutoPeople> writer) {
        return remoteStepBuilderFactory.get("workerStep")
                .inputChannel(requests())
                .<TutoPeople, TutoPeople>chunk(5)
                .reader(reader(null))
                .processor(processor())
                .writer(writer)
                .build();
    }
}
