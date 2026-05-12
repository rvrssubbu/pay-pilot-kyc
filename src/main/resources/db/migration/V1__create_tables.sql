CREATE TABLE kyc_details (
    merchant_id VARCHAR(50) PRIMARY KEY,
    status VARCHAR(30),
    updated_by VARCHAR(50)
);

CREATE TABLE kyc_audit (
    id BIGSERIAL PRIMARY KEY,
    merchant_id VARCHAR(50),
    old_status VARCHAR(30),
    new_status VARCHAR(30),
    updated_by VARCHAR(50),
    timestamp TIMESTAMP
);

CREATE TABLE outbox_event (
    event_id VARCHAR(100) PRIMARY KEY,
    event_type VARCHAR(100),
    payload TEXT,
    status VARCHAR(30),
    retry_count INT,
    created_at TIMESTAMP
);