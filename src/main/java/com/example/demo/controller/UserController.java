package com.example.demo.controller;

import com.example.demo.model.tables.pojos.User;
import com.example.demo.service.UserService;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController {
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void findById(@NotNull Context ctx) {
    LOG.debug("url : {}", ctx.fullUrl());
    javax.servlet.http.HttpServletRequest request = ctx.req;
    Long id = ctx.pathParam("id", Long.class).get();
    userService.findById(id).ifPresentOrElse(user -> {
      ctx.json(user);
    },() -> {
      ctx.status(404);
    });
  }

  public void findAll(@NotNull Context ctx) {
    List<User> users = userService.findAll();
    ctx.json(users);
  }

  public void store(@NotNull Context ctx) {
    User user = ctx.bodyAsClass(User.class);
    User storedUser = userService.save(user);
    ctx.json(storedUser);
  }

  public void remove(@NotNull Context ctx) {
    Long id = ctx.pathParam("id", Long.class).get();
    userService.remove(id);
    ctx.status(200);
  }

  public void bindEndpoints(@NotNull Javalin app) {
    app.routes(() -> {
      ApiBuilder.path("/api", () -> {
        // GET /api/users
        ApiBuilder.get("/users", this::findAll);
        ApiBuilder.path("/user", () -> {
          // GET /api/user/:id
          ApiBuilder.get(":id", this::findById);
          // POST /api/user
          ApiBuilder.post(this::store);
          // DELETE /api/user/:id
          ApiBuilder.delete(":id", this::remove);
        });
      });
    });
  }

}
