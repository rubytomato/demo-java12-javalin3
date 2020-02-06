package com.example.demo.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Sex {
  MALE("M"),
  FEMALE("F")
  ;

  private static final Map<String, Sex> LOOKUP;

  static {
    Map<String, Sex> temp = new HashMap<>();
    for (Sex sex : Sex.values()) {
      temp.put(sex.value, sex);
    }
    LOOKUP = Collections.unmodifiableMap(temp);
  }

  public static Sex lookupByValue(String value) {
    return LOOKUP.get(value);
  }

  private String value;

  Sex(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
