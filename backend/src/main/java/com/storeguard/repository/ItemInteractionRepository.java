package com.storeguard.repository;

import com.storeguard.model.ItemInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemInteractionRepository extends JpaRepository<ItemInteraction, Long> {
    List<ItemInteraction> findBySessionIdOrderByOccurredAtAsc(String sessionId);
}
