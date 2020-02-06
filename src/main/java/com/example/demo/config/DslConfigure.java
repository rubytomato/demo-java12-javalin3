package com.example.demo.config;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

import javax.sql.DataSource;
import java.util.Optional;

public interface DslConfigure {
  DSLContext getDslContext();

  DataSource getDataSource();
  SQLDialect getSqlDialect();
  Settings getSettings();

  // optional settings
  Optional<ConnectionProvider> getConnectionProvider();
  Optional<TransactionProvider> getTransactionProvider();

  default Configuration configuration() {
    Configuration config = new DefaultConfiguration();
    config
        .set(getSqlDialect())
        .set(getSettings());
    getConnectionProvider().ifPresentOrElse(cp -> {
      config.set(cp);
      getTransactionProvider().ifPresent(tp -> {
        config.set(tp);
      });
    }, () -> {
      config.set(getDataSource());
    });
    return config;
  }
}
