DROP TABLE IF EXISTS sample_db.user;
CREATE TABLE IF NOT EXISTS sample_db.user (
  id              BIGINT       AUTO_INCREMENT          COMMENT 'ユーザーID',
  nick_name       VARCHAR(60)  NOT NULL                COMMENT 'ニックネーム',
  sex             CHAR(1)      NOT NULL                COMMENT '性別 M:男性 F:女性',
  prefecture_id   TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '都道府県 0:不明、1:北海道 - 8:九州・沖縄',
  email           VARCHAR(120)                         COMMENT 'メールアドレス',
  memo            TEXT                                 COMMENT '備考欄',
  create_at       DATETIME     NOT NULL DEFAULT NOW(),
  update_at       DATETIME     NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id)
);

CREATE INDEX idx_sex on sample_db.user (sex) USING BTREE;
CREATE INDEX idx_pref on sample_db.user (prefecture_id) USING BTREE;
