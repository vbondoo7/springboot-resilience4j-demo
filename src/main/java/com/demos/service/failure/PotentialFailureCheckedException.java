package com.demos.service.failure;

public interface PotentialFailureCheckedException {
	boolean occur() throws Exception;
}