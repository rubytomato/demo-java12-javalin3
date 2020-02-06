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

public class HelloController {
  private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

  public HelloController() {
  }

  public void getHello(@NotNull Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("message", "Hello World");
    model.put("now", LocalDateTime.now());
    model.put("q", "ABC");
    HelloForm form = new HelloForm();
    form.setMessage("デフォルトメッセージ");
    model.put("form", form);
    ctx.render("hello_world.html", model);
  }

  public void postHello(@NotNull Context ctx) {
    String q = ctx.queryParam("q", String.class, "*Q*").get();
    String message = ctx.formParam("message", String.class, "*M*").get();
    LOG.debug("q:{}, message:{}", q, message);
    ctx.contentType("text/plain;charset=utf-8");
    ctx.result(message);
  }

  public void bindEndpoints(@NotNull Javalin app) {
    app.routes(() -> {
      ApiBuilder.path("/hello", () -> {
        ApiBuilder.get(this::getHello);
        ApiBuilder.post(this::postHello);
      });
    });
  }

}
