package com.example.demo.converter;

import org.jooq.Converter;

public class SexConverter implements Converter<String, Sex> {
  private static final long serialVersionUID = -4578644212051646490L;

  @Override
  public Sex from(String value) {
    return Sex.lookupByValue(value);
  }

  @Override
  public String to(Sex sex) {
    return sex.getValue();
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Class<Sex> toType() {
    return Sex.class;
  }

}
