package com.example.demo.service.impl;

import com.example.demo.AppServiceException;
import com.example.demo.model.tables.pojos.User;
import com.example.demo.model.tables.records.UserRecord;
import com.example.demo.service.UserService;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.model.Tables.USER;

public class UserServiceImpl implements UserService {
  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
  private final DSLContext dsl;

  public UserServiceImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public Optional<User> findById(Long id) {
    LOG.debug("find by Id : {}", id);
    UserRecord record = dsl
        .selectFrom(USER)
        .where(USER.ID.eq(id))
        .fetchOne();
    if (record != null) {
      return Optional.of(record.into(User.class));
    }
    return Optional.empty();
  }

  @Override
  public List<User> findByNickName(String nickName) {
    LOG.debug("find by nickName : {}", nickName);
    Result<UserRecord> result = dsl
        .selectFrom(USER)
        .where(USER.NICK_NAME.contains(nickName))
        .orderBy(USER.NICK_NAME.asc())
        .fetch();
    return result.into(User.class);
  }

  @Override
  public List<User> findAll() {
    LOG.debug("findAll");
    Result<UserRecord> result = dsl
        .selectFrom(USER)
        .orderBy(USER.ID.desc())
        .fetch();
    return result.into(User.class);
  }

  @Override
  public User save(User user) {
    LOG.debug("save : {}", user);
    UserRecord result = dsl.transactionResult(conf -> {
      if (user.getId() == null || user.getId().equals(0L)) {
        return DSL.using(conf)
            .insertInto(USER,
                USER.NICK_NAME,
                USER.SEX,
                USER.PREFECTURE_ID,
                USER.EMAIL,
                USER.MEMO)
            .values(
                user.getNickName(),
                user.getSex(),
                user.getPrefectureId(),
                user.getEmail(),
                user.getMemo())
            .returning()
            .fetchOne();
      } else {
        return DSL.using(conf)
            .update(USER)
            .set(USER.NICK_NAME, user.getNickName())
            .set(USER.SEX, user.getSex())
            .set(USER.PREFECTURE_ID, user.getPrefectureId())
            .set(USER.EMAIL, user.getEmail())
            .set(USER.MEMO, user.getMemo())
            .set(USER.UPDATE_AT, LocalDateTime.now())
            .where(USER.ID.eq(user.getId()))
            .returning()
            .fetchOne();
      }
    });
    LOG.debug("save result : {}", result);
    if (result == null) {
      throw new AppServiceException("[save] Not Found saved user record.");
    }
    return result.into(User.class);
  }

  @Override
  public void remove(Long id) {
    LOG.debug("remove by id : {}", id);
    int result = dsl.transactionResult(conf -> {
      int count = DSL.using(conf)
          .selectCount()
          .from(USER)
          .where(USER.ID.eq(id))
          .execute();
      if (count != 1) {
        return count;
      }
      return DSL.using(conf)
          .deleteFrom(USER)
          .where(USER.ID.eq(id))
          .execute();
    });
    LOG.debug("remove result : {}", result);
    if (result == 0) {
      throw new AppServiceException("[remove] Not Found delete user record.");
    }
  }

}
