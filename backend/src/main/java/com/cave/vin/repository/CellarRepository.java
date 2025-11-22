package com.cave.vin.repository;

import com.cave.vin.domain.Cellar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellarRepository extends JpaRepository<Cellar, Long> {
    List<Cellar> findByUserId(Long userId);
}
