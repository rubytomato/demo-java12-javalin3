# javalin 3.4.1 demo application with JDK 13

Development environment

* OpenJDK 13.0.2
* Javalin 3.7.0
* MySQL 8.0.17
* JUnit 5.5.1
* Maven 3.6.1

```
openjdk version "13.0.2" 2020-01-14
OpenJDK Runtime Environment (build 13.0.2+8)
OpenJDK 64-Bit Server VM (build 13.0.2+8, mixed mode, sharing)
```

## compile

```
mvn clean package
```

## run

```
java -jar .\target\
```

Specify a locale

```
java -D -jar
```

## database (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS sample_db
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
;

CREATE USER IF NOT EXISTS 'test_user'@'localhost'
  IDENTIFIED BY 'test_user'
  PASSWORD EXPIRE NEVER
;

GRANT ALL ON sample_db.* TO 'test_user'@'localhost';
```

```sql
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS  user (
  id BIGINT AUTO_INCREMENT                    COMMENT 'ユーザーID',
  nick_name VARCHAR(60) NOT NULL              COMMENT 'ニックネーム',
  sex CHAR(1) NOT NULL                        COMMENT '性別 M:男性 F:女性',
  prefecture_id TINYINT(1) NOT NULL DEFAULT 0 COMMENT '都道府県 0:不明、1:北海道 - 8:九州・沖縄',
  email VARCHAR(120)                          COMMENT 'メールアドレス',
  memo TEXT                                   COMMENT '備考欄',
  create_at DATETIME NOT NULL DEFAULT NOW(),
  update_at DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id)
)
ENGINE = INNODB
DEFAULT CHARSET = UTF8MB4
COMMENT = 'ユーザーテーブル';

CREATE INDEX idx_sex on user (sex) USING BTREE;
CREATE INDEX idx_pref on user (prefecture_id) USING BTREE;
```

## static page

http://localhost:7000/app/
http://localhost:7000/app/index.html

## template page

http://localhost:7000/app/hello

## api

http://localhost:7000/app/api/users
http://localhost:7000/app/api/user/1
