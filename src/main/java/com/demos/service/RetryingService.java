package com.demos.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demos.model.Flight;
import com.demos.model.SearchRequest;
import com.demos.model.SearchResponse;
import com.demos.service.failure.PotentialFailure;
import com.demos.service.failure.PotentialFailureCheckedException;

import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RetryingService {

	@Autowired
	private FlightSearchService remoteSearchService;

	@Autowired
	private RetryRegistry registry;

	@Retry(name = "basic")
	public List<Flight> basicExample(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	@Retry(name = "throwingException")
	public List<Flight> searchFlightsThrowingException(SearchRequest request) throws Exception {
		return remoteSearchService.searchFlightsThrowingException(request);
	}

	@Retry(name = "predicateExample")
	public SearchResponse predicateExample(SearchRequest request) throws IOException {
		return remoteSearchService.httpSearchFlights(request);
	}

	@Retry(name = "intervalFunctionRandomExample")
	public List<Flight> intervalFunctionRandom(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	@Retry(name = "intervalFunctionExponentialExample")
	public List<Flight> intervalFunctionExponential(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	@Retry(name = "asyncSearchExample")
	public List<Flight> asyncSearchExample(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	@Retry(name = "loggedRetryExample")
	public List<Flight> loggedRetryExample(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	public void setPotentialFailure(PotentialFailure potentialFailure) {
		remoteSearchService.setPotentialFailure(potentialFailure);
	}

	public void setPotentialFailureCheckedException(PotentialFailureCheckedException potentialFailureCheckedException) {
		remoteSearchService.setPotentialFailureCheckedException(potentialFailureCheckedException);
	}

	@PostConstruct
	public void postConstruct() {
		registry
			.retry("loggedRetryExample")
			.getEventPublisher()
			.onRetry(System.out::println);
	}

	@Retry(name = "fallbackExample", fallbackMethod = "localCacheFlightSearch")
	public List<Flight> fallbackExample(SearchRequest request) {
		return remoteSearchService.searchFlights(request);
	}

	private List<Flight> localCacheFlightSearch(SearchRequest request, RuntimeException re) {
		log.info("FALLBACK - Returning search results from fallback");
		return Arrays.asList(new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()));
	}
}