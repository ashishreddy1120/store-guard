CREATE TABLE item_interaction (
  id BIGSERIAL PRIMARY KEY,
  session_id VARCHAR(64) NOT NULL,
  occurred_at TIMESTAMPTZ NOT NULL,
  camera_id VARCHAR(64) NOT NULL,
  action VARCHAR(32) NOT NULL,  -- PICK, PUT_BACK, PLACE_IN_BAG, SCANNED
  product_id VARCHAR(128),
  quantity INT NOT NULL DEFAULT 1,
  confidence DOUBLE PRECISION NOT NULL DEFAULT 0,
  CONSTRAINT fk_item_session FOREIGN KEY (session_id)
    REFERENCES person_session(session_id) ON DELETE CASCADE
);
CREATE INDEX idx_item_interactions_session ON item_interaction(session_id);
CREATE INDEX idx_item_interactions_time ON item_interaction(occurred_at);
