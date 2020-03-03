package com.magnarox.batch.repositories;

import com.magnarox.batch.entities.TutoPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutoPeopleRepository extends JpaRepository<TutoPeople, Long> {
}
