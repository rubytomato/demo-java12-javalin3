package com.example.demo.renderer;

import io.javalin.http.Context;
import io.javalin.plugin.rendering.FileRenderer;
import org.jetbrains.annotations.NotNull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CustomThymeleafRenderer implements FileRenderer {

  private final TemplateEngine templateEngine;

  public CustomThymeleafRenderer() {
    templateEngine = templateEngine();
  }

  @Override
  public String render(@NotNull String filePath, @NotNull Map<String, Object> model, @NotNull Context ctx) {
    System.out.println("filePath : " + filePath + " locale : " + ctx.req.getLocale().toString());
    WebContext context = new WebContext(ctx.req, ctx.res, ctx.req.getServletContext(), ctx.req.getLocale());
    context.setVariables(model);
    return templateEngine.process(filePath, context);
  }

  private TemplateEngine templateEngine() {
    System.out.println("new instance");
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.addDialect(new Java8TimeDialect());
    return templateEngine;
  }

  private ITemplateResolver templateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setCacheable(true);
    templateResolver.setCacheTTLMs(TimeUnit.MINUTES.toMillis(30));
    templateResolver.setTemplateMode(TemplateMode.HTML);
    return templateResolver;
  }

}
