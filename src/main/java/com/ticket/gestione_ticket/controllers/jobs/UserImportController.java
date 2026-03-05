package com.ticket.gestione_ticket.controllers.jobs;

import com.ticket.gestione_ticket.jobs.UserImportJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class UserImportController {

    private final UserImportJob userImportJob;

    @PostMapping("/import-users")
    public ResponseEntity<String> triggerUserImport() {
        userImportJob.importUsers();
        return ResponseEntity.ok("Job ImportUser eseguito.");
    }
}