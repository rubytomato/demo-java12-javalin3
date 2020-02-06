package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultTransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Optional;

public class DslContextHolder implements DslConfigure, Serializable {
  private static final long serialVersionUID = -8276859195238889686L;
  private static final Logger LOG = LoggerFactory.getLogger(DslContextHolder.class);

  private static DslContextHolder instance;

  private final DSLContext dslContext;
  private final DataSource dataSource;
  private final Settings settings;
  private final ConnectionProvider connectionProvider;
  private final TransactionProvider transactionProvider;

  private DslContextHolder() {
    dataSource = createDataSource();
    settings = createSettings();
    connectionProvider = new DataSourceConnectionProvider(dataSource);
    transactionProvider = new DefaultTransactionProvider(connectionProvider, true);
    // transactionProvider = new ThreadLocalTransactionProvider(connectionProvider, true);
    dslContext = DSL.using(configuration());
  }

  public static DslContextHolder getInstance() {
    if (instance == null) {
      synchronized (DslContextHolder.class) {
        if (instance == null) {
          instance = new DslContextHolder();
          LOG.debug("DSL Context create : {}", instance.getDslContext().toString());
        }
      }
    }
    return instance;
  }

  public static void destroy() {
    if (instance != null) {
      synchronized (DslContextHolder.class) {
        if (instance != null) {
          LOG.debug("DSL Context destroy : {}", instance.getDslContext().toString());
          instance.dslContext.close();
          instance = null;
        }
      }
    }
  }

  DataSource createDataSource() {
    DbPropertyLoader prop = new DbPropertyLoader("datasource.properties");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(prop.getJdbcUrl());
    config.setUsername(prop.getUserName());
    config.setPassword(prop.getPassword());
    config.setPoolName("hikari-cp");
    config.setAutoCommit(false);
    config.setConnectionTestQuery("select 1");
    config.setMaximumPoolSize(10);
    DataSource dataSource = new HikariDataSource(config);
    LOG.debug("DataSource create : {}", dataSource);
    return dataSource;
  }

  Settings createSettings() {
    Settings settings = new Settings()
        .withStatementType(StatementType.STATIC_STATEMENT)
        .withQueryTimeout(10)
        .withMaxRows(1000)
        .withFetchSize(20)
        .withExecuteLogging(true);
    LOG.debug("Settings create : {}", settings);
    return settings;
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
    return SQLDialect.MYSQL;
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
