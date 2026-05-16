# Outbox Retry/DLQ Flow

1. Events are first saved into outbox_event table.
2. Scheduler polls pending events every 5 seconds.
3. Failed publishes are retried.
4. After 3 failures event moves to DLQ state.
5. eventId acts as deduplication key.

## Topics

- kyc-events
- kyc-events-retry
- kyc-events-dlq