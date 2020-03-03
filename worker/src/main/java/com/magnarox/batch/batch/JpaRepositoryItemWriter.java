package com.magnarox.batch.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaRepositoryItemWriter<T>
        implements ItemWriter<T> {

    /**
     * Spring Data JPA : JpaRepository.
     */
    private JpaRepository<T, ?> jpaRepository;

    public JpaRepositoryItemWriter(JpaRepository<T, ?> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        jpaRepository.saveAll(items);
        jpaRepository.flush();
    }

}
