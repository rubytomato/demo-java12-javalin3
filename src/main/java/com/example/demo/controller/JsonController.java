package com.example.demo.controller;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JsonController {
  private static final Logger LOG = LoggerFactory.getLogger(JsonController.class);

  public JsonController() {
  }

  public void getJson(@NotNull Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("message", "Hello World");
    model.put("now", LocalDateTime.now());
    ctx.json(model);
  }

  public void postJson(@NotNull Context ctx) {
    Map<String, Object> model = ctx.bodyAsClass(Map.class);
    System.out.println(model.get("message"));
    System.out.println(model.get("now"));
    ctx.status(200);
  }

  public void bindEndpoints(@NotNull Javalin app) {
    app.routes(() -> {
      ApiBuilder.path("/json", () -> {
        ApiBuilder.get(this::getJson);
        ApiBuilder.post(this::postJson);
      });
    });
  }

}
