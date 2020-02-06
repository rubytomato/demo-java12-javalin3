package com.example.demo;

public class AppServiceException extends RuntimeException {
  private static final long serialVersionUID = 5545837767637518658L;

  public AppServiceException() {
  }
  public AppServiceException(String message) {
    super(message);
  }
  public AppServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
