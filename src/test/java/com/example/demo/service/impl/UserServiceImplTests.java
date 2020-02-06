package com.example.demo.service.impl;

import com.example.demo.TestDslContextHolder;
import com.example.demo.TestH2Server;
import com.example.demo.converter.Prefecture;
import com.example.demo.converter.Sex;
import com.example.demo.model.tables.pojos.User;
import com.example.demo.model.tables.records.UserRecord;
import com.example.demo.service.TestService;
import com.example.demo.service.UserService;
import org.assertj.core.groups.Tuple;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.demo.model.SampleDb.SAMPLE_DB;
import static com.example.demo.model.Tables.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTests implements TestService {

  private DSLContext dsl;
  private TestH2Server h2;

  private List<String> initScripts = List.of(
      "./h2/sql/01_schema.sql",
      "./h2/sql/02_table.sql"
  );

  private List<String> clearScripts = List.of(
      "./h2/sql/04_table.sql",
      "./h2/sql/05_schema.sql"
  );

  @Override
  public DSLContext getDSLContext() {
    return dsl;
  }

  @Override
  public void setDSLContext(DSLContext dsl) {
    this.dsl = dsl;
  }

  @BeforeAll
  public void setupSchema() {
    System.out.println("setup class");
    h2 = new TestH2Server();
    h2.start();
    h2.runScript(initScripts);
    setDSLContext(TestDslContextHolder.getInstance().getDslContext());
    // createSchema(SAMPLE_DB);
  }

  @AfterAll
  public void tearDownSchema() {
    System.out.println("teardown class");
    // dropSchema(SAMPLE_DB);
    TestDslContextHolder.destroy();
    h2.runScript(clearScripts);
    h2.shutdown();
  }

  @BeforeEach
  public void setup() {
    System.out.println("setup");
    h2.status();
  }

  @AfterEach
  public void tearDown() {
    System.out.println("teardown");
    cleanUp(List.of(USER));
  }

  @Test
  public void findById() {
    System.out.println("findById");
    try {
      dsl.transaction(conf -> {
        // setup test data
        DSL.using(conf)
            .insertInto(USER,
                USER.ID,
                USER.NICK_NAME,
                USER.SEX,
                USER.PREFECTURE_ID,
                USER.EMAIL,
                USER.MEMO)
            .values(1L, "nick name AAA", Sex.FEMALE, Prefecture.HOKKAIDO, "test.user_a@example.com", "memo aaa")
            .values(2L, "nick name BBB", Sex.MALE, Prefecture.TOHOKU, "test.user_b@example.com", null)
            .values(3L, "nick name CCC", Sex.FEMALE, Prefecture.KANTOU, "test.user_c@example.com", "memo ccc")
            .execute();

        // exercise
        UserService sut = new UserServiceImpl(DSL.using(conf));
        Optional<User> actual = sut.findById(1L);

        // verify
        assertThat(actual.isPresent()).isTrue();
        actual.ifPresent(user -> {
          assertThat(user)
              .extracting(
                  "id",
                  "nickName",
                  "sex",
                  "prefectureId",
                  "email",
                  "memo")
              .contains(
                  1L,
                  "nick name AAA",
                  Sex.FEMALE,
                  Prefecture.HOKKAIDO,
                  "test.user_a@example.com",
                  "memo aaa");
        });

        testDataRollback();
      });
    } catch (DataAccessException e) {
      isFailed(e);
    }
  }

  @Test
  public void findByNickName() {
    System.out.println("findByNickName");
    try {
      dsl.transaction(conf -> {
        // setup test data
        DSL.using(conf)
            .insertInto(USER,
                USER.ID,
                USER.NICK_NAME,
                USER.SEX,
                USER.PREFECTURE_ID,
                USER.EMAIL,
                USER.MEMO)
            .values(1L, "nick name AAA", Sex.FEMALE, Prefecture.HOKKAIDO, "test.user_a@example.com", "memo aaa")
            .values(2L, "nick name BBB", Sex.MALE, Prefecture.TOHOKU, "test.user_b@example.com", null)
            .values(3L, "nick name CCC", Sex.FEMALE, Prefecture.KANTOU, "test.user_c@example.com", "memo ccc")
            .execute();

        // exercise
        UserService sut = new UserServiceImpl(DSL.using(conf));
        List<User> actual = sut.findByNickName("BBB");

        // verify
        assertThat(actual).hasSize(1);
        assertThat(actual)
            .extracting(
                "id",
                "nickName",
                "sex",
                "prefectureId",
                "email",
                "memo")
            .contains(Tuple.tuple(2L, "nick name BBB", Sex.MALE, Prefecture.TOHOKU, "test.user_b@example.com", null));

        testDataRollback();
      });
    } catch (DataAccessException e) {
      isFailed(e);
    }
  }

  @Test
  public void save() {
    System.out.println("save --->");
    try {
      dsl.transaction(conf -> {
        // exercise
        User testData = new User(null, "nick name DDD", Sex.MALE, Prefecture.KANTOU, "test.user_d@example.com", null, null, null);
        UserService sut = new UserServiceImpl(DSL.using(conf));
        User expected = sut.save(testData);

        // verify
        Optional<User> actual = sut.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        actual.ifPresent(user -> {
          assertThat(user)
              .extracting(
                  "id",
                  "nickName",
                  "sex",
                  "prefectureId",
                  "email",
                  "memo")
              .contains(
                  expected.getId(),
                  expected.getNickName(),
                  expected.getSex(),
                  expected.getPrefectureId(),
                  expected.getEmail(),
                  expected.getMemo());
        });

        testDataRollback();
      });
    } catch (DataAccessException e) {
      isFailed(e);
    }
    System.out.println("<--- save");
  }

  @Test
  public void remove() {
    System.out.println("remove --->");
    try {
      dsl.transaction(conf -> {
        // setup
        Long userId = 1L;
        DSL.using(conf)
            .insertInto(USER,
                USER.ID,
                USER.NICK_NAME,
                USER.SEX,
                USER.PREFECTURE_ID,
                USER.EMAIL,
                USER.MEMO)
            .values(
                userId,
                "nick name EEE",
                Sex.MALE,
                Prefecture.CHUBU,
                "test.user_e@example.com",
                "memo eee")
            .execute();

        // exercise
        UserService sut = new UserServiceImpl(DSL.using(conf));
        sut.remove(userId);

        // verify
        UserRecord actual = DSL.using(conf)
            .selectFrom(USER)
            .where(USER.ID.eq(userId))
            .fetchOne();
        assertThat(actual).isNull();

        testDataRollback();
      });
    } catch (DataAccessException e) {
      isFailed(e);
    }
    System.out.println("<--- remove");
  }

}
