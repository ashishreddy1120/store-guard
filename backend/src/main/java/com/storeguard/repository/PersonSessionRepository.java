package com.storeguard.repository;

import com.storeguard.model.PersonSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PersonSessionRepository extends JpaRepository<PersonSession, Long> {
    Optional<PersonSession> findBySessionId(String sessionId);
    List<PersonSession> findByActiveTrueOrderByStartTimeDesc();
}
