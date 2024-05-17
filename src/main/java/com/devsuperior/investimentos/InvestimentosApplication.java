package com.devsuperior.investimentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvestimentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestimentosApplication.class, args);
	}

}
