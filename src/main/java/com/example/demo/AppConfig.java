package com.example.demo;

import com.example.demo.config.SessionConfig;
import io.javalin.core.JavalinConfig;
import io.javalin.core.event.EventListener;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AppConfig {
  private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

  // application configuration
  static final Consumer<JavalinConfig> CONFIGURATIONS = (config) -> {
    config.contextPath = "/app";
    config.showJavalinBanner = true;

    config.sessionHandler(new SessionConfig().fileSessionHandler());

    config.enableWebjars();

    config.addStaticFiles("/public");
    //config.addStaticFiles("/public/images");
    //config.addStaticFiles("D:\\var\\static", Location.EXTERNAL);

    config.requestLogger((ctx, ms) -> {
      log.debug("request URL:{}, UA:{}, AL:{}", ctx.fullUrl(), ctx.userAgent(), ctx.header("Accept-Language"));
    });

    config.registerPlugin(new RouteOverviewPlugin("overview"));
    config.enableDevLogging();
  };

  // lifecycle events
  static final Consumer<EventListener> EVENTS = (event) -> {
    event.serverStarting(() -> {
      log.debug("server starting");
    });
    event.serverStarted(() -> {
      log.debug("server started");
    });
    event.serverStopping(() -> {
      log.debug("server stopping");
    });
    event.serverStopped(() -> {
      log.debug("server stopped");
    });
  };

}
