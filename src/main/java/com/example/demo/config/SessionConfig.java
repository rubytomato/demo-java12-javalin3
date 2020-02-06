package com.example.demo.config;

import org.eclipse.jetty.server.session.DatabaseAdaptor;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.FileSessionDataStore;
import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Supplier;

public class SessionConfig {

  public Supplier<SessionHandler> sqlSessionHandler(final String driver, final String url) {
    return () -> {
      SessionHandler sessionHandler = new SessionHandler();
      SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
      sessionCache.setSessionDataStore(
          jdbcDataStoreFactory(driver, url).getSessionDataStore(sessionHandler)
      );
      sessionHandler.setSessionCache(sessionCache);
      sessionHandler.setHttpOnly(true);
      // make additional changes to your SessionHandler here
      return sessionHandler;
    };
  }

  private JDBCSessionDataStoreFactory jdbcDataStoreFactory(final String driver, final String url) {
    DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
    databaseAdaptor.setDriverInfo(driver, url);
    JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
    jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
    return jdbcSessionDataStoreFactory;
  }

  public Supplier<SessionHandler> fileSessionHandler() {
    return () -> {
      SessionHandler sessionHandler = new SessionHandler();
      SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
      sessionCache.setSessionDataStore(fileSessionDataStore());
      sessionHandler.setSessionCache(sessionCache);
      sessionHandler.setHttpOnly(true);
      return sessionHandler;
    };
  }

  private FileSessionDataStore fileSessionDataStore() {
    FileSessionDataStore fileSessionDataStore = new FileSessionDataStore();
    Path storeDir = Paths.get(System.getProperty("java.io.tmpdir"), "javalin-session-store");
    createStore(storeDir);
    fileSessionDataStore.setStoreDir(storeDir.toFile());
    return fileSessionDataStore;
  }

  private void createStore(Path path) {
    try {
      cleanUp(path);
      Files.deleteIfExists(path);
      Files.createDirectory(path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void cleanUp(Path path) throws IOException {
    Files.walkFileTree(path, new FileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println("file delete : " + file.toAbsolutePath());
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("dir delete : " + dir.toAbsolutePath());
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

}
