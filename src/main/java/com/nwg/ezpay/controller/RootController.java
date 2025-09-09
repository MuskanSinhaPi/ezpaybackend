package com.nwg.ezpay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RootController handles basic welcome or health endpoints. Author: Muskan
 * Version: 1.0 Last Revised: 22-Aug-2025
 */
@RestController
public class RootController {

	/**
	 * Root GET endpoint.
	 * 
	 * @return Welcome message
	 */
	@GetMapping("/")
	public String hello() {
		return "Welcome to EZPay Banking API!";
	}
}
