package com.demos.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demos.model.Flight;
import com.demos.model.SearchRequest;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RateLimitService {

	@Autowired
	private FlightSearchService remoteSearchService;

	@Autowired
	private RateLimiterRegistry registry;
	
	@PostConstruct
	public void postConstruct() {
		registry
			.rateLimiter("loggedRateLimiterExample")
			.getEventPublisher()
			.onFailure(System.out::println)
			.onSuccess(System.out::println)
			.onEvent(System.out::println);
	}
	
	@RateLimiter(name = "flightSearch", fallbackMethod = "localCacheFlightSearch")
	public List<Flight> searchFlightsWithDelayedResponse(SearchRequest request) throws Exception {
		return remoteSearchService.searchFlightsWithDelayedResponse(request);
	}

	private List<Flight> localCacheFlightSearch(SearchRequest request, RuntimeException re) {
		log.info("FALLBACK - Returning search results from fallback");
		return Arrays.asList(new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()));
	}
}