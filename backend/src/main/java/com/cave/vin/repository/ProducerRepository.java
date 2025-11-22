package com.cave.vin.repository;

import com.cave.vin.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    java.util.Optional<Producer> findByName(String name);
}
