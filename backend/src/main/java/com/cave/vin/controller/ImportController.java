package com.cave.vin.controller;

import com.cave.vin.service.CsvImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final CsvImportService csvImportService;
    private final com.cave.vin.repository.UserRepository userRepository;

    public ImportController(CsvImportService csvImportService, com.cave.vin.repository.UserRepository userRepository) {
        this.csvImportService = csvImportService;
        this.userRepository = userRepository;
    }

    @PostMapping("/csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file,
            @RequestParam("cellarName") String cellarName,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a CSV file to upload.");
        }

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in.");
        }

        String email = principal.getAttribute("email");
        com.cave.vin.domain.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            csvImportService.importCsv(file, user, cellarName);
            return ResponseEntity.ok("File uploaded and processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file: " + e.getMessage());
        }
    }
}
