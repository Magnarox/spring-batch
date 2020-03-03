package com.magnarox.batch.manager.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class BatchJobExecutionListener extends JobExecutionListenerSupport {

    public BatchJobExecutionListener() {
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB FINISHED !!!");
        }
    }
}
