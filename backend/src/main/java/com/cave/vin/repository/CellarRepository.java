package com.cave.vin.repository;

import com.cave.vin.domain.Cellar;
import com.cave.vin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellarRepository extends JpaRepository<Cellar, Long> {
    List<Cellar> findByUserId(Long userId);

    java.util.Optional<Cellar> findByNameAndUser(String name, User user);
}
