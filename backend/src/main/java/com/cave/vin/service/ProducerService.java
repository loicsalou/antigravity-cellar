package com.cave.vin.service;

import com.cave.vin.domain.Producer;
import com.cave.vin.repository.ProducerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ProducerService {
    private final ProducerRepository producerRepository;

    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public List<Producer> getAllProducers() {
        return producerRepository.findAll();
    }

    public Producer createProducer(Producer producer) {
        return producerRepository.save(producer);
    }
}

@RestController
@RequestMapping("/api/producers")
@CrossOrigin(origins = "http://localhost:4200")
class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping
    public List<Producer> getAllProducers() {
        return producerService.getAllProducers();
    }

    @PostMapping
    public Producer createProducer(@RequestBody Producer producer) {
        return producerService.createProducer(producer);
    }
}
