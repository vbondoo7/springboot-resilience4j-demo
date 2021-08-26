package com.demos.predicate;

import java.util.function.Predicate;

import com.demos.model.SearchResponse;

public class ConditionalRetryPredicate implements Predicate<SearchResponse> {
	@Override
	public boolean test(SearchResponse searchResponse) {
		if (searchResponse.getErrorCode() != null) {
			System.out.println("Search returned error code = " + searchResponse.getErrorCode());
			return searchResponse.getErrorCode().equals("FS-167");
		}
		return false;
	}
}