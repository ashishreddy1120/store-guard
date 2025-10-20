CREATE TABLE person_session (
  id BIGSERIAL PRIMARY KEY,
  session_id VARCHAR(64) UNIQUE NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  end_time   TIMESTAMPTZ,
  active     BOOLEAN NOT NULL DEFAULT TRUE,
  suspicious_score DOUBLE PRECISION NOT NULL DEFAULT 0,
  last_camera_id   VARCHAR(64),
  metadata JSONB
);
CREATE INDEX idx_person_session_active ON person_session(active);
CREATE INDEX idx_person_session_session_id ON person_session(session_id);
