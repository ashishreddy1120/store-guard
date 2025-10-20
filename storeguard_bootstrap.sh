#!/usr/bin/env bash
set -euo pipefail

REPO=$(pwd)
echo "Bootstrapping StoreGuard in $REPO"

# Folders
mkdir -p .github/workflows infra \
  backend/src/main/java/com/storeguard/{config,controller,dto,model,repository,service,util} \
  backend/src/main/resources/db/migration \
  vision

# .gitignore
cat > .gitignore <<'GIT'
target/
*.iml
.idea/
.classpath
.project
.settings/
.mvn/wrapper/maven-wrapper.jar
.DS_Store
infra/*.env
*.log
GIT

# .editorconfig
cat > .editorconfig <<'EC'
root = true
[*]
end_of_line = lf
insert_final_newline = true
charset = utf-8
indent_style = space
indent_size = 2
[*.java]
indent_size = 4
EC

# README
cat > README.md <<'MD'
# StoreGuard
StoreGuard tracks shopper sessions and item pick/put/scan events in retail stores, correlating those with POS receipts to identify possible losses.

> Legal & Privacy: Production deployments must follow local laws, post signage, minimize retention, and avoid biometric face recognition unless legally vetted.
MD

# docker-compose (Postgres)
cat > docker-compose.yml <<'YML'
version: "3.9"
services:
  db:
    image: postgres:16
    container_name: storeguard_db
    environment:
      POSTGRES_USER: storeguard
      POSTGRES_PASSWORD: storeguard
      POSTGRES_DB: storeguard
    ports:
      - "5432:5432"
    volumes:
      - dbdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U storeguard"]
      interval: 5s
      timeout: 3s
      retries: 20
volumes:
  dbdata:
YML

# Sample env template
mkdir -p infra
cat > infra/local.env.example <<'ENV'
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5432/storeguard
DB_USER=storeguard
DB_PASS=storeguard
ENV

# Backend pom.xml (Spring Boot + JPA + Postgres)
cat > backend/pom.xml <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.4</version>
  </parent>
  <groupId>com.storeguard</groupId>
  <artifactId>storeguard-backend</artifactId>
  <version>0.1.0</version>
  <properties>
    <java.version>21</java.version>
  </properties>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-core</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
POM

# Backend application.yml
cat > backend/src/main/resources/application.yml <<'YML'
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/storeguard}
    username: ${DB_USER:storeguard}
    password: ${DB_PASS:storeguard}
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  flyway:
    enabled: true
    locations: classpath:db/migration
server:
  port: 8080
logging:
  level:
    root: INFO
    com.storeguard: DEBUG
YML

# Initial Flyway migrations
cat > backend/src/main/resources/db/migration/V1__init.sql <<'SQL'
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
SQL

cat > backend/src/main/resources/db/migration/V2__item_interactions.sql <<'SQL'
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
SQL

echo "✅ Bootstrap basic structure created."
