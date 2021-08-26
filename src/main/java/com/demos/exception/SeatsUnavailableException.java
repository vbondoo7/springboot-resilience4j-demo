package com.demos.exception;

public class SeatsUnavailableException extends FlightServiceBaseException {
	public SeatsUnavailableException(String message) {
		super(message);
	}
}