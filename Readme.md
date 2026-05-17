# Pay Pilot KYC System

Enterprise-style Spring Boot application implementing:

- KYC State Machine
- AOP-based Audit Logging
- Outbox Pattern
- Retry & DLQ Convention
- Swagger API Documentation
- PostgreSQL Persistence
- Scheduling & Event Processing

---

# Tech Stack

- Java 17
- Spring Boot
- Spring AOP
- Spring Scheduler
- Spring Data JPA
- PostgreSQL
- Flyway
- Swagger / OpenAPI
- JUnit 5
- Maven

---

# Implemented Sprint Slices

## Slice 2 – KYC State Machine + AOP Audit

### Features

- Controlled KYC lifecycle transitions
- Illegal transition validation
- Centralized audit logging using AOP
- Audit history APIs
- Unit tests for state transitions and audit aspect

### Supported KYC Flow

```text
DRAFT → SUBMITTED
SUBMITTED → IN_REVIEW
IN_REVIEW → APPROVED / REJECTED
```

### Invalid transitions are blocked

Example:

```text
APPROVED → DRAFT ❌
```

---

## Slice 5 – Outbox + Retry/DLQ Convention

### Features

- Outbox table for reliable event publishing
- Scheduler-based event processing
- Retry mechanism
- DLQ handling
- Topic naming conventions
- Admin APIs for outbox and DLQ records

### Event Flow

```text
API Request
    ↓
Save Business Data
    ↓
Save Outbox Event
    ↓
Scheduler Picks Pending Events
    ↓
Publish Attempt

Success → SENT
Failure → RETRY
Max Retry Exceeded → DLQ
```

---

# Kafka Topic Naming Convention

```text
kyc-events
kyc-events-retry
kyc-events-dlq
```

---

# Idempotency

The system uses:

```text
eventId
```

as the deduplication key to support idempotent event processing semantics and prevent duplicate consumer impact during at-least-once delivery.

---

# At-Least-Once Delivery

The Outbox pattern guarantees:

- events are never lost
- failed events are retried
- permanently failing events move to DLQ

---

# APIs

## KYC APIs

### Update KYC Status

```http
POST /paypilot/kyc/update
```

### Get Merchant Audit History

```http
GET /paypilot/kyc/history/{merchantId}
```

### Get Complete Audit History

```http
GET /paypilot/kyc/history
```

---

## Outbox APIs

### Get All Outbox Events

```http
GET /paypilot/outbox
```

### Get DLQ Events

```http
GET /paypilot/outbox/dlq
```

---

# Swagger UI

```text
http://localhost:8081/paypilot/swagger-ui.html
```

---

# Database

PostgreSQL is used for persistence.

Database name:

```text
paypilot_kyc
```

---

# Running the Application

## Build

```bash
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

---

# Testing

JUnit 5 test cases are implemented for:

- KYC State Machine
- AOP Audit Aspect
- Outbox Service
- Scheduler
- Controllers
- Exception Handler

---

# Key Concepts Demonstrated

- State Machine Pattern
- Aspect Oriented Programming (AOP)
- Outbox Pattern
- Retry/DLQ Architecture
- At-Least-Once Delivery
- Idempotency
- Scheduling
- Layered Architecture
- REST APIs
- Persistence using JPA

---

# Future Enhancements

- Real Kafka Integration
- Distributed Idempotency Store
- Kafka Consumer Group Handling
- Dead Letter Replay Mechanism
- Authentication & Authorization
- Docker & Kubernetes Deployment

---

# Author

Subbu