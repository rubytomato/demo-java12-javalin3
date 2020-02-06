package com.example.demo;

import com.example.demo.config.DbPropertyLoader;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestH2Server implements AutoCloseable {

  private final Server server;

  private final String jdbcUrl;
  private final String userName;
  private final String password;

  public TestH2Server() {
    DbPropertyLoader prop = new DbPropertyLoader("test-datasource.properties");
    jdbcUrl = prop.getJdbcUrl();
    userName = prop.getUserName();
    password = prop.getPassword();
    String[] params = new String[] {"-tcp", "-tcpPort", "9092", "-baseDir", "./h2"};
    try {
      server = Server.createTcpServer(params);
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void start() {
    System.out.println("server start");
    try {
      server.start();
      status();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void runScript(List<String> scripts) {
    try {
      for (String script : scripts) {
        System.out.println("run script : " + script);
        RunScript.execute(jdbcUrl, userName, password, script, StandardCharsets.UTF_8, false);
      }
    } catch (SQLException e) {
      System.err.println("run script : " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void shutdown() {
    if (server.isRunning(true)) {
      System.out.println("server shutdown");
      server.shutdown();
    }
  }

  public void status() {
    System.out.println(server.getStatus());
  }

  // for test
  public static void main(String ... args) {
    try (TestH2Server h2 = new TestH2Server()) {
      h2.start();
      h2.status();
      h2.runScript(List.of("./h2/sql/01_schema.sql", "./h2/sql/02_table.sql", "./h2/sql/03_data.sql"));
      h2.test();
      h2.runScript(List.of("./h2/sql/04_table.sql", "./h2/sql/05_schema.sql"));
    }
    System.exit(0);
  }

  public void test() {
    System.out.println("test");
    try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password)) {
      PreparedStatement ps = conn.prepareStatement("select * from sample_db.user");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        System.out.println("id : " + rs.getLong("id"));
        System.out.println("nick_name : " + rs.getString("nick_name"));
        System.out.println("sex : " + rs.getString("sex"));
        System.out.println("prefecture_id : " + rs.getInt("prefecture_id"));
        System.out.println("email : " + rs.getString("email"));
        System.out.println("memo : " + rs.getString("memo"));
        System.out.println("createAt : " + rs.getTimestamp("create_at"));
        System.out.println("updateAt : " + rs.getTimestamp("update_at"));
      }
    } catch (SQLException e) {
      System.err.println("test error : " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    shutdown();
  }

}
