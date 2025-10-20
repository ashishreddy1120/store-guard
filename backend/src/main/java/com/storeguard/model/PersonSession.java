package com.storeguard.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "person_session")
public class PersonSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", unique = true, nullable = false, length = 64)
    private String sessionId;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "suspicious_score", nullable = false)
    private Double suspiciousScore = 0.0;

    @Column(name = "last_camera_id")
    private String lastCameraId;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSessionId(){return sessionId;} public void setSessionId(String s){this.sessionId=s;}
    public Instant getStartTime(){return startTime;} public void setStartTime(Instant t){this.startTime=t;}
    public Instant getEndTime(){return endTime;} public void setEndTime(Instant t){this.endTime=t;}
    public boolean isActive(){return active;} public void setActive(boolean a){this.active=a;}
    public Double getSuspiciousScore(){return suspiciousScore;} public void setSuspiciousScore(Double d){this.suspiciousScore=d;}
    public String getLastCameraId(){return lastCameraId;} public void setLastCameraId(String c){this.lastCameraId=c;}
    public String getMetadata(){return metadata;} public void setMetadata(String m){this.metadata=m;}
}
