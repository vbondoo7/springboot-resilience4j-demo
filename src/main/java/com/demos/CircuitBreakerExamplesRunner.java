package com.demos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demos.model.SearchRequest;
import com.demos.service.CircuitBreakerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CircuitBreakerExamplesRunner {

  @Autowired
  private CircuitBreakerService service;

  public static void main(String[] args) throws Exception {
    CircuitBreakerExamplesRunner runner = new CircuitBreakerExamplesRunner();
    runner.run();
  }

  public void run() throws Exception {
    System.out.println("Running RateLimit examples");

    System.out.println(
        "------------------------- basicExample ---------------------------------------------");
    basicExample();
    System.out.println("----------------------------------------------------------------------");
  }

  private void basicExample() throws Exception {
    SearchRequest request1 = new SearchRequest("NYC", "LAX", "07/30/2021");
    
    for(int i = 0; i<= 50; i++) {
    	CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
    		try {
    			service.searchFlightsWithDelayedResponse(request1);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    	if(i%10 == 0)
    		TimeUnit.SECONDS.sleep(1);
    }
    log.info("Calls finished");
  }

}