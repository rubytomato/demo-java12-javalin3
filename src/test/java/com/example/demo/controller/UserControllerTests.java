package com.example.demo.controller;

import com.example.demo.config.DslContextHolder;
import com.example.demo.config.JacksonConfig;
import com.example.demo.converter.Prefecture;
import com.example.demo.converter.Sex;
import com.example.demo.model.tables.pojos.User;
import com.example.demo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {

  Javalin app;
  int port;
  ObjectMapper objectMapper;

  @BeforeAll
  public void setup() {
    port = 7000;
    app = Javalin.create().start(port);
    objectMapper = JacksonConfig.getObjectMapper();
    JavalinJackson.configure(objectMapper);
  }

  @AfterAll
  public void tearDown() {
    app.stop();
  }

  @Test
  public void findById() throws Exception {
    // setup
    new UserController(new UserServiceImpl(DslContextHolder.getInstance().getDslContext()))
        .bindEndpoints(app);

    // exercise
    Long userId = 1L;
    String testTargetUrl = String.format("http://localhost:%d/api/user/%d", port, userId);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(testTargetUrl))
        .header("Accept", "application/json")
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // verify
    assertThat(response.statusCode()).isEqualTo(200);
    User actual = objectMapper.readValue(response.body(), User.class);
    assertThat(actual)
        .extracting(
            "id",
            "nickName",
            "sex",
            "prefectureId",
            "email",
            "memo")
        .contains(
            1L,
            "test user 1",
            Sex.MALE,
            Prefecture.HOKKAIDO,
            "test.user1@example.com",
            "memo 1"
        );
  }

}
