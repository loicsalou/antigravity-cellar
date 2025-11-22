package com.cave.vin.repository;

import com.cave.vin.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WineRepository extends JpaRepository<Wine, Long>, JpaSpecificationExecutor<Wine> {
    // SpecificationExecutor for advanced search
    java.util.Optional<Wine> findByNameAndProducerAndVintage(String name, com.cave.vin.domain.Producer producer,
            Integer vintage);
}
