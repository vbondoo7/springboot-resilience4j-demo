package com.demos;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demos.model.SearchRequest;
import com.demos.service.BulkheadService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BulkheadExamplesRunner {

  @Autowired
  private BulkheadService service;

  public static void main(String[] args) throws Exception {
    BulkheadExamplesRunner runner = new BulkheadExamplesRunner();
    runner.run();
  }

  public void run() throws Exception {
    System.out.println("Running bulkhead examples");

    System.out.println(
        "------------------------- basicExample ---------------------------------------------");
    basicExample();
    System.out.println("----------------------------------------------------------------------");
  }

  private void basicExample() throws Exception {
	  SearchRequest request1 = new SearchRequest("NYC", "LAX", "07/30/2021");
	    
	    for(int i = 0; i<= 10; i++) {
	    	CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
	    		try {
	    			service.searchFlightsWithDelayedResponse(request1);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	});
	    }
    log.info("Calls finished");
  }

}