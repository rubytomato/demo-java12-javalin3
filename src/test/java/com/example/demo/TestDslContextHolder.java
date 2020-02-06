package com.example.demo;

import com.example.demo.config.DbPropertyLoader;
import com.example.demo.config.DslConfigure;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultTransactionProvider;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Optional;

public class TestDslContextHolder implements DslConfigure, Serializable {
  private static final long serialVersionUID = -5728144007357037196L;

  private static TestDslContextHolder instance;

  private final DSLContext dslContext;
  private final DataSource dataSource;
  private final Settings settings;
  private final ConnectionProvider connectionProvider;
  private final TransactionProvider transactionProvider;

  public TestDslContextHolder() {
    dataSource = createDataSource();
    settings = createSettings();
    connectionProvider = new DataSourceConnectionProvider(dataSource);
    transactionProvider = new DefaultTransactionProvider(connectionProvider, true);
    // transactionProvider = new ThreadLocalTransactionProvider(connectionProvider, true);
    dslContext = DSL.using(configuration());
  }

  public static TestDslContextHolder getInstance() {
    if (instance == null) {
      synchronized (TestDslContextHolder.class) {
        if (instance == null) {
          instance = new TestDslContextHolder();
        }
      }
    }
    return instance;
  }

  public static void destroy() {
    if (instance != null) {
      synchronized (TestDslContextHolder.class) {
        if (instance != null) {
          instance.dslContext.close();
          instance = null;
        }
      }
    }
  }

  DataSource createDataSource() {
    DbPropertyLoader prop = new DbPropertyLoader("test-datasource.properties");
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL(prop.getJdbcUrl());
    dataSource.setUser(prop.getUserName());
    dataSource.setPassword(prop.getPassword());
    return dataSource;
  }

  Settings createSettings() {
    return new Settings()
        .withStatementType(StatementType.STATIC_STATEMENT)
        .withQueryTimeout(10)
        .withMaxRows(1000)
        .withFetchSize(20)
        .withExecuteLogging(true)
        .withDebugInfoOnStackTrace(true)
        .withRenderFormatted(true);
  }

  @Override
  public DSLContext getDslContext() {
    return this.dslContext;
  }

  @Override
  public DataSource getDataSource() {
    return this.dataSource;
  }

  @Override
  public SQLDialect getSqlDialect() {
    return SQLDialect.H2;
  }

  @Override
  public Settings getSettings() {
    return this.settings;
  }

  @Override
  public Optional<ConnectionProvider> getConnectionProvider() {
    //
    return Optional.ofNullable(this.connectionProvider);
  }

  @Override
  public Optional<TransactionProvider> getTransactionProvider() {
    //
    return Optional.ofNullable(this.transactionProvider);
  }

}
