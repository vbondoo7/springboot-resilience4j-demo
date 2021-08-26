package com.demos.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.demos.model.Flight;
import com.demos.model.SearchRequest;
import com.demos.model.SearchResponse;
import com.demos.service.failure.NoCheckedExceptionFailure;
import com.demos.service.failure.NoFailure;
import com.demos.service.failure.PotentialFailure;
import com.demos.service.failure.PotentialFailureCheckedException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlightSearchService {

	PotentialFailure potentialFailure = new NoFailure();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
	
	public static int counter = 0;
	
	PotentialFailureCheckedException potentialFailureCheckedException = new NoCheckedExceptionFailure();

	public List<Flight> searchFlights(SearchRequest request) {
		log.info("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
		potentialFailure.occur();

		List<Flight> flights = Arrays.asList(
				new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo()));
		log.info("ACTUAL RESPONSE - Flight search successful");
		return flights;
	}

	public List<Flight> searchFlightsThrowingException(SearchRequest request) throws Exception {

		log.info("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
		try {
			if (!potentialFailureCheckedException.occur()) {
				List<Flight> flights = Arrays.asList(
						new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
						new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
						new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
						new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo()));
				log.info("ACTUAL RESPONSE - Flight search successful");
				return flights;
			}
		} catch (RuntimeException re) {
			throw new Exception("Exception when searching for flights");
		}
		return Collections.emptyList();
	}
	
	public List<Flight> searchFlightsWithDelayedResponse(SearchRequest request) throws Exception {
		log.info("Searching for flights; current time = " + LocalDateTime.now().format(formatter));
		potentialFailure.occur();

		counter++;
		if(counter >= 20 && counter <= 35)
			throw new RuntimeException("Inetrnal Service Error");
		List<Flight> flights = Arrays.asList(
				new Flight("XY 765", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 781", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 732", request.getFlightDate(), request.getFrom(), request.getTo()),
				new Flight("XY 746", request.getFlightDate(), request.getFrom(), request.getTo()));
		TimeUnit.SECONDS.sleep(10);
		log.info("ACTUAL RESPONSE - Flight search successful");
		return flights;
	}

	public void setPotentialFailure(PotentialFailure potentialFailure) {
		this.potentialFailure = potentialFailure;
	}

	public void setPotentialFailureCheckedException(PotentialFailureCheckedException potentialFailureCheckedException) {
		this.potentialFailureCheckedException = potentialFailureCheckedException;
	}

	public SearchResponse httpSearchFlights(SearchRequest request) throws IOException {
		log.info("Searching for flights; current time = " + LocalDateTime.now().format(formatter));

		String date = request.getFlightDate();
		String from = request.getFrom();
		String to = request.getTo();
		if (request.getFlightDate().equals("01/25/2021")) { // Simulating an error scenario
			try {
				if (!potentialFailure.occur()) {
					List<Flight> flights = Arrays.asList(new Flight("XY 765", date, from, to),
							new Flight("XY 781", date, from, to), new Flight("XY 732", date, from, to),
							new Flight("XY 746", date, from, to));
					log.info("ACTUAL RESPONSE - Flight search successful");
					SearchResponse response = new SearchResponse();
					response.setFlights(flights);
					return response;
				}
			} catch (RuntimeException re) {
				log.error("Flight data initialization in progress, cannot search at this time");
				SearchResponse response = new SearchResponse();
				response.setErrorCode("FS-167");
				response.setFlights(Collections.emptyList());
				return response;
			}
		}

		potentialFailure.occur();
		List<Flight> flights = Arrays.asList(new Flight("XY 765", date, from, to), new Flight("XY 781", date, from, to),
				new Flight("XY 732", date, from, to), new Flight("XY 746", date, from, to));
		log.info("ACTUAL RESPONSE - Flight search successful");
		SearchResponse response = new SearchResponse();
		response.setFlights(flights);
		return response;
	}
}