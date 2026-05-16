package com.bank.pay_pilot_kyc.constants;

public class KafkaTopicsConstants {

    private KafkaTopicsConstants(){

    }

    public static final String
            KYC_EVENTS_TOPIC =
            "kyc-events";

    public static final String
            KYC_RETRY_TOPIC =
            "kyc-events-retry";

    public static final String
            KYC_DLQ_TOPIC =
            "kyc-events-dlq";
}
