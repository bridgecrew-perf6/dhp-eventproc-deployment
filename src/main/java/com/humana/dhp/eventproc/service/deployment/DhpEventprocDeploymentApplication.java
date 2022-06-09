package com.humana.dhp.eventproc.service.deployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DhpEventprocDeploymentApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DhpEventprocDeploymentApplication.class, args);
	}

}
