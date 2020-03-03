package com.magnarox.batch.manager.repositories;

import com.magnarox.batch.manager.entities.TutoPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutoPeopleRepository extends JpaRepository<TutoPeople, Long> {
}
