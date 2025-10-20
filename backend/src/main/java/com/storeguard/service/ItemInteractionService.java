package com.storeguard.service;

import com.storeguard.model.ItemInteraction;
import com.storeguard.repository.ItemInteractionRepository;
import com.storeguard.repository.PersonSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemInteractionService {

    private final ItemInteractionRepository repo;
    private final PersonSessionRepository sessionRepo;

    public ItemInteractionService(ItemInteractionRepository repo,
                                  PersonSessionRepository sessionRepo) {
        this.repo = repo;
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public ItemInteraction record(ItemInteraction interaction) {
        sessionRepo.findBySessionId(interaction.getSessionId()).orElseThrow(
            () -> new IllegalArgumentException("Unknown sessionId: " + interaction.getSessionId())
        );
        return repo.save(interaction);
    }

    @Transactional(readOnly = true)
    public List<ItemInteraction> listForSession(String sessionId) {
        return repo.findBySessionIdOrderByOccurredAtAsc(sessionId);
    }
}
