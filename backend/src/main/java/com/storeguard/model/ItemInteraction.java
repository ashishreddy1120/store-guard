package com.storeguard.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "item_interaction")
public class ItemInteraction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="session_id", nullable = false, length = 64)
    private String sessionId;

    @Column(name="occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name="camera_id", nullable = false)
    private String cameraId;

    @Column(nullable = false, length = 32)
    private String action; // PICK, PUT_BACK, PLACE_IN_BAG, SCANNED

    private String productId;
    private int quantity = 1;
    private double confidence = 0.0;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSessionId(){return sessionId;} public void setSessionId(String s){this.sessionId=s;}
    public Instant getOccurredAt(){return occurredAt;} public void setOccurredAt(Instant o){this.occurredAt=o;}
    public String getCameraId(){return cameraId;} public void setCameraId(String c){this.cameraId=c;}
    public String getAction(){return action;} public void setAction(String a){this.action=a;}
    public String getProductId(){return productId;} public void setProductId(String p){this.productId=p;}
    public int getQuantity(){return quantity;} public void setQuantity(int q){this.quantity=q;}
    public double getConfidence(){return confidence;} public void setConfidence(double c){this.confidence=c;}
}
