package com.magnarox.batch.batch;

import com.magnarox.batch.entities.TutoPeople;
import com.magnarox.batch.repositories.TutoPeopleRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchJobExecutionListener extends JobExecutionListenerSupport {

    TutoPeopleRepository peopleRepository;

    public BatchJobExecutionListener(TutoPeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB FINISHED! Time to verify the results");

            List<TutoPeople> all = this.peopleRepository.findAll();

            System.out.println("People Find : " + all.size());

            all.forEach(x -> System.out.println(x.getPersonId() + " : " + x.getFirstName() + " " + x.getLastName()));
        }
    }

}