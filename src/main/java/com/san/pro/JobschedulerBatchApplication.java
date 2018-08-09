package com.san.pro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobschedulerBatchApplication {
	
	private static final Logger log = LoggerFactory.getLogger(JobschedulerBatchApplication.class);

	public static void main(String[] args) {
		try {
			SpringApplication.run(JobschedulerBatchApplication.class, args);
			log.info("--- Application started successfully ---");
		} catch (Exception e) {
			log.error("something went wrong !!!");
		}
		
	}
}
