package com.bank.pay_pilot_kyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PayPilotKycApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayPilotKycApplication.class, args);
	}

}
