package com.mugide.cafe_popp_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CafePoppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafePoppBackendApplication.class, args);
	}

}


// Admin: admin@restaurant.com / Admin@12345
// Waiter: jane.waiter@restaurant.com / Waiter@12345
// Cashier: bob.cashier@restaurant.com / Cashier@12345
// Chef: marco.kitchen@restaurant.com / Chef@12345