package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public class JacksonConfig {
  private final ObjectMapper objectMapper;
  private static final JacksonConfig INSTANCE = new JacksonConfig();

  public static JacksonConfig getInstance() {
    return INSTANCE;
  }

  public static ObjectMapper getObjectMapper() {
    return INSTANCE.objectMapper;
  }

  private JacksonConfig() {
    objectMapper = createObjectMapper();
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat(dateFormat());
    return objectMapper;
  }

  private SimpleDateFormat dateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  }

}
