package com.example.demo;

import com.example.demo.renderer.CustomThymeleafRenderer;
import com.example.demo.config.DslContextHolder;
import com.example.demo.config.JacksonConfig;
import com.example.demo.controller.HelloController;
import com.example.demo.controller.JsonController;
import com.example.demo.controller.UserController;
import com.example.demo.service.impl.UserServiceImpl;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.rendering.JavalinRenderer;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  static final Logger LOG = LoggerFactory.getLogger(App.class);

  public static void main(String... args) {
    Javalin app = Javalin
        .create(AppConfig.CONFIGURATIONS)
        .events(AppConfig.EVENTS)
        .start(7000);

    // json
    JavalinJackson.configure(JacksonConfig.getObjectMapper());
    final JsonController jsonController = new JsonController();
    jsonController.bindEndpoints(app);

    // thymeleaf
    JavalinRenderer.register(new CustomThymeleafRenderer(), ".html");
    final HelloController helloController = new HelloController();
    helloController.bindEndpoints(app);

    // database (jOOQ)
    final DSLContext dsl = DslContextHolder.getInstance().getDslContext();
    final UserController userController = new UserController(new UserServiceImpl(dsl));
    userController.bindEndpoints(app);

    // exception mapping
    app.exception(AppServiceException.class, (e, ctx) -> {
      LOG.error("handling AppServiceException", e);
      ctx.result("Application Error : " + e.getMessage()).status(500);
    });
  }

}
