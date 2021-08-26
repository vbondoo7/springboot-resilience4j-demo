package com.demos;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demos.model.Flight;
import com.demos.model.SearchRequest;
import com.demos.model.SearchResponse;
import com.demos.service.RetryingService;
import com.demos.service.failure.FailNTimes;
import com.demos.service.failure.FailNTimesCheckedException;

@Component
public class RetryExamplesRunner {

  @Autowired
  private RetryingService service;

  public static void main(String[] args) {
    RetryExamplesRunner runner = new RetryExamplesRunner();
    runner.run();
  }

  public void run() {
    System.out.println("Running retry examples");

    System.out.println(
        "------------------------- basicExample ---------------------------------------------");
    basicExample();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "------------------------- checkedExceptionExample ---------------------------------------------");
    checkedExceptionExample();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "--------------------------- predicateExample -------------------------------------------");
    predicateExample();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "---------------------------- intervalFunction_Random ------------------------------------------");
    intervalFunction_Random();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "----------------------------- intervalFunction_Exponential -----------------------------------------");
    intervalFunction_Exponential();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "----------------------------- retryEventsExample -----------------------------------------");
    retryEventsExample();
    System.out.println("----------------------------------------------------------------------");

    System.out.println(
        "----------------------------- fallbackExample -----------------------------------------");
    fallbackExample();
    System.out.println("----------------------------------------------------------------------");
  }

  private void retryEventsExample() {
    service.setPotentialFailure(new FailNTimes(2));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    List<Flight> flights = service.loggedRetryExample(request);
    System.out.println(flights);
  }

  private void intervalFunction_Exponential() {
    service.setPotentialFailure(new FailNTimes(5));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    List<Flight> flights = service.intervalFunctionExponential(request);
    System.out.println(flights);
  }

  private void intervalFunction_Random() {
    service.setPotentialFailure(new FailNTimes(2));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    List<Flight> flights = service.intervalFunctionRandom(request);
    System.out.println(flights);
  }

  private void predicateExample() {
    service.setPotentialFailure(new FailNTimes(1));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    try {
      SearchResponse response = service.predicateExample(request);
      List<Flight> flights = response.getFlights();
      System.out.println(flights);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void checkedExceptionExample() {
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    service.setPotentialFailureCheckedException(new FailNTimesCheckedException(2));

    try {
      List<Flight> flights = service.searchFlightsThrowingException(request);
      System.out.println(flights);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void basicExample() {
    service.setPotentialFailure(new FailNTimes(1));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    List<Flight> flights = service.basicExample(request);
    System.out.println(flights);
  }

  private void fallbackExample() {
    service.setPotentialFailure(new FailNTimes(4));
    SearchRequest request = new SearchRequest("NYC", "LAX", "07/31/2021");
    List<Flight> flights = service.fallbackExample(request);
    System.out.println(flights);

  }
}