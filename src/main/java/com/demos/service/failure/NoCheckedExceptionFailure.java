package com.demos.service.failure;

public class NoCheckedExceptionFailure implements PotentialFailureCheckedException {
	@Override
	public boolean occur() throws Exception {
		return false;
	}
}