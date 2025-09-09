package com.nwg.ezpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class EzpaybackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzpaybackendApplication.class, args);
	}

}
