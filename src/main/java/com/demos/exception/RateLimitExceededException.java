package com.demos.exception;

public class RateLimitExceededException extends FlightServiceBaseException {
	String errorCode;

	public RateLimitExceededException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}