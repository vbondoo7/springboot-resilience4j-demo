package com.demos.exception;

public class FlightServiceBaseException extends RuntimeException {
	public FlightServiceBaseException(String message) {
		super(message);
	}
}