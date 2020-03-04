package com.magnarox.batch.manager;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EntryPoint {

    @Autowired
    private Job importUserJob;

    private final SimpleJobLauncher jobLauncher;

    public EntryPoint(JobRepository jobRepository) {
        this.jobLauncher = new SimpleJobLauncher();
        this.jobLauncher.setJobRepository(jobRepository);
        this.jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    }

    @GetMapping("start")
    public ResponseEntity<String> start() {
        try {
            final Map<String, JobParameter> parameters = new HashMap<>();
            final JobParameter param = new JobParameter(UUID.randomUUID().toString(), true);
            parameters.put("UUID", param);
            final JobParameters jobParameters = new JobParameters(parameters);

            final JobExecution jobExecution = this.jobLauncher.run(importUserJob, jobParameters);
            final Long jobId = jobExecution.getJobId();

            return ResponseEntity.ok("Job " + jobId + " is started.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
