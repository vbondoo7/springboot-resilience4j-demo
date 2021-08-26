package com.demos;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	@Autowired
	private RetryExamplesRunner retryExamplesRunner;
	
	@Autowired
	private RateLimitExamplesRunner rateLimitExamplesRunner;

	@Autowired
	private BulkheadExamplesRunner bulkheadExamplesRunner;

	@Autowired
	private CircuitBreakerExamplesRunner circuitBreakerExamplesRunner;
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
	public void runExamples() throws Exception {
    	TimeUnit.SECONDS.sleep(2);
    	circuitBreakerExamplesRunner.run();
		//retryExamplesRunner.run();
		//bulkheadExamplesRunner.run();
    	//rateLimitExamplesRunner.run();
	}

}

