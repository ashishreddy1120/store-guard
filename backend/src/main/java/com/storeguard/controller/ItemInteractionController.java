package com.storeguard.controller;

import com.storeguard.dto.ItemInteractionRequest;
import com.storeguard.model.ItemInteraction;
import com.storeguard.service.ItemInteractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/item-interactions")
public class ItemInteractionController {

    private final ItemInteractionService service;

    public ItemInteractionController(ItemInteractionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ItemInteraction> create(@Valid @RequestBody ItemInteractionRequest req) {
        ItemInteraction i = new ItemInteraction();
        i.setSessionId(req.sessionId());
        i.setCameraId(req.cameraId());
        i.setOccurredAt(req.occurredAt() == null ? Instant.now() : req.occurredAt());
        i.setAction(req.action());
        i.setProductId(req.productId());
        i.setQuantity(req.quantity() == 0 ? 1 : req.quantity());
        i.setConfidence(req.confidence());
        return ResponseEntity.ok(service.record(i));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<ItemInteraction>> list(@PathVariable String sessionId) {
        return ResponseEntity.ok(service.listForSession(sessionId));
    }
}
