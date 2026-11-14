package com.medicore.api;

import org.springframework.boot.SpringApplication;

public class TestMedicoreBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(MedicoreBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
