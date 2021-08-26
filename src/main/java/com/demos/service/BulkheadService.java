package com.demos.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demos.model.Flight;
import com.demos.model.SearchRequest;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BulkheadService {

	@Autowired
	private FlightSearchService remoteSearchService;

	@Autowired
	private BulkheadRegistry registry;
	
	@PostConstruct
	public void postConstruct() {
		registry
			.bulkhead("loggedBulkheadExample")
			.getEventPublisher()
			.onCallPermitted(System.out::println)
			.onCallRejected(System.out::println)
			.onEvent(System.out::println);
	}
	
	@Bulkhead(name = "flightSearch", fallbackMethod = "localCacheFlightSearch")
	public List<Flight> searchFlightsWithDelayedResponse(SearchRequest request) throws Exception {
		return remoteSearchService.searchFlightsWithDelayedResponse(request);
	}

	private List<Flight> localCacheFlightSearch(SearchRequest request, RuntimeException re) {
		log.info("FALLBACK - Returning search results from fallback");
		return Arrays.asList(new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()));
	}
}