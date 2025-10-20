package com.storeguard.controller;

import com.storeguard.dto.EndSessionRequest;
import com.storeguard.dto.HeartbeatRequest;
import com.storeguard.model.PersonSession;
import com.storeguard.repository.PersonSessionRepository;
import com.storeguard.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService service;
    private final PersonSessionRepository repo;

    public SessionController(SessionService service, PersonSessionRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<PersonSession> heartbeat(@Valid @RequestBody HeartbeatRequest req) {
        return ResponseEntity.ok(service.heartbeat(req.sessionId(), req.cameraId()));
    }

    @PostMapping("/end")
    public ResponseEntity<Void> end(@Valid @RequestBody EndSessionRequest req) {
        service.endSession(req.sessionId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PersonSession>> listActive() {
        return ResponseEntity.ok(repo.findByActiveTrueOrderByStartTimeDesc());
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<PersonSession> getBySession(@PathVariable String sessionId) {
        return repo.findBySessionId(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
