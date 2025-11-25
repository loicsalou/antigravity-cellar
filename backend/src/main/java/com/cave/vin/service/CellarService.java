package com.cave.vin.service;

import com.cave.vin.domain.Cellar;
import com.cave.vin.domain.User;
import com.cave.vin.repository.CellarRepository;
import com.cave.vin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CellarService {

    private final CellarRepository cellarRepository;
    private final UserRepository userRepository;

    public CellarService(CellarRepository cellarRepository, UserRepository userRepository) {
        this.cellarRepository = cellarRepository;
        this.userRepository = userRepository;
    }

    public List<Cellar> getCellarsForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return cellarRepository.findByUserId(user.getId());
    }

    public Cellar getCellarById(Long id) {
        return cellarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cellar not found with id: " + id));
    }

    @Transactional
    public Cellar createCellar(String email, Cellar cellar) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        cellar.setUser(user);
        return cellarRepository.save(cellar);
    }

    @Transactional
    public void deleteCellar(Long id) {
        cellarRepository.deleteById(id);
    }
}
