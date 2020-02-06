package com.example.demo.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class DbPropertyLoader {
  private final Properties prop;

  private static final String JDBC_URL = "datasource.jdbcUrl";
  private static final String USER_NAME = "datasource.userName";
  private static final String PASSWORD = "datasource.password";

  public DbPropertyLoader(String fileName) {
    prop = loadProperties(fileName);
  }

  public String getJdbcUrl() {
    return prop.getProperty(JDBC_URL);
  }

  public String getUserName() {
    return prop.getProperty(USER_NAME);
  }

  public String getPassword() {
    return prop.getProperty(PASSWORD);
  }

  private Properties loadProperties(String fileName) {
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName)) {
      if (stream == null) {
        throw new IOException("Not Found : " + fileName);
      }
      Properties prop = new Properties();
      prop.load(stream);
      return prop;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
