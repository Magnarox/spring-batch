package com.magnarox.batch.manager.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableBatchIntegration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private RemotePartitioningManagerStepBuilderFactory remoteStepBuilderFactory;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression("batchtopic"));
        return IntegrationFlows
                .from(requests())
                .handle(kafkaMessageHandler)
                .get();
    }

    @Bean
    public ResourcePartitioner partitioner() throws IOException {
        return new ResourcePartitioner().setResources(getResources());
    }

    @Bean
    public Job partitionerJob(final BatchJobExecutionListener listener, final Step managerStep) {
        return jobBuilderFactory.get("partitionerJob")
                .listener(listener)
                .start(managerStep)
                .build();
    }

    @Bean
    public Step managerStep() throws IOException {
        return remoteStepBuilderFactory.get("managerStep")
                .partitioner("workerStep", partitioner())
                .gridSize(1)
                .outputChannel(requests())
                .build();
    }

    private Resource[] getResources() throws IOException {
        final File inputDir = new ClassPathResource("input").getFile();
        if (!inputDir.exists() || !inputDir.isDirectory())
            throw new IOException("Bad input configuration");

        return Arrays.stream(inputDir.list()).map(x -> "input/" + x).map(ClassPathResource::new).toArray(ClassPathResource[]::new);
    }
}
