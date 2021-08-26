package com.demos.service.failure;

public class NoFailure implements PotentialFailure {
	@Override
	public boolean occur() {
		return false;
	}
}