package com.example.demo.controller;

import java.io.Serializable;

public class HelloForm implements Serializable {
  private static final long serialVersionUID = 1804121159014258328L;

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "HelloForm{" +
        "message='" + message + '\'' +
        '}';
  }
}
