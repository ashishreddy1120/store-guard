package com.storeguard.service;

import com.storeguard.model.PersonSession;
import com.storeguard.repository.PersonSessionRepository;
import com.storeguard.util.ClockProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SessionService {
    private final PersonSessionRepository repo;
    private final ClockProvider clockProvider;

    public SessionService(PersonSessionRepository repo, ClockProvider clockProvider) {
        this.repo = repo;
        this.clockProvider = clockProvider;
    }

    @Transactional
    public PersonSession heartbeat(String sessionId, String cameraId) {
        var now = Instant.now(clockProvider.getClock());
        var session = repo.findBySessionId(sessionId).orElseGet(() -> {
            var s = new PersonSession();
            s.setSessionId(sessionId);
            s.setStartTime(now);
            s.setActive(true);
            return s;
        });
        session.setLastCameraId(cameraId);
        session.setEndTime(now);
        return repo.save(session);
    }

    @Transactional
    public void endSession(String sessionId) {
        repo.findBySessionId(sessionId).ifPresent(s -> {
            s.setActive(false);
            s.setEndTime(Instant.now(clockProvider.getClock()));
            repo.save(s);
        });
    }
}
