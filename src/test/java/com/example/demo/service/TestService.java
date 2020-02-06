package com.example.demo.service;

import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.util.List;

public interface TestService {

  DSLContext getDSLContext();
  void setDSLContext(DSLContext dsl);

  default void createSchema(Schema schema) {
    Queries queries = getDSLContext().ddl(schema);
    for (Query query : queries.queries()) {
      getDSLContext().execute(query);
    }
  }

  default void dropSchema(Schema schema) {
    List<Table<?>> tables = schema.getTables();
    for (Table<?> table : tables) {
      getDSLContext().dropTableIfExists(table).execute();
    }
    getDSLContext().dropSchemaIfExists(schema).execute();
  }

  default void cleanUp(List<Table<?>> tables) {
    getDSLContext().transaction(conf -> {
      for (Table table : tables) {
        DSL.using(conf).deleteFrom(table).execute();
      }
    });
  }

  default void testDataRollback() {
    throw new DataAccessException("rollback");
  }

  default void isFailed(RuntimeException e) {
    if (!e.getMessage().equals("rollback")) {
      System.err.println(e);
      throw e;
    }
  }

}
