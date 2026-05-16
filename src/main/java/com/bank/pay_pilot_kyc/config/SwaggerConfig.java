package com.bank.pay_pilot_kyc.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI payPilotOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()
                                .title("Pay Pilot KYC API")

                                .description(
                                        "Enterprise KYC System with "
                                                + "AOP Auditing, "
                                                + "Outbox Retry, "
                                                + "DLQ Handling "
                                                + "and State Machine Validation"
                                )

                                .version("v1.0")

                                /*.contact(
                                        new Contact()
                                                .name("Pay Pilot Team")
                                                .email("team@paypilot.com")
                                                .url("https://paypilot.com")
                                )

                                .license(
                                        new License()
                                                .name("Internal Enterprise License")
                                )*/
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Project Documentation")
                                .url("https://github.com/rvrssubbu/pay-pilot-kyc")
                );
    }
}