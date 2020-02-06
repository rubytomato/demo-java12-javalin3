/*
 * This file is generated by jOOQ.
 */
package com.example.demo.model.tables.pojos;


import com.example.demo.converter.Prefecture;
import com.example.demo.converter.Sex;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * ユーザーテーブル
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class User implements Serializable {

    private static final long serialVersionUID = 1547078637;

    private Long          id;
    private String        nickName;
    private Sex           sex;
    private Prefecture    prefectureId;
    private String        email;
    private String        memo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public User() {}

    public User(User value) {
        this.id = value.id;
        this.nickName = value.nickName;
        this.sex = value.sex;
        this.prefectureId = value.prefectureId;
        this.email = value.email;
        this.memo = value.memo;
        this.createAt = value.createAt;
        this.updateAt = value.updateAt;
    }

    public User(
        Long          id,
        String        nickName,
        Sex           sex,
        Prefecture    prefectureId,
        String        email,
        String        memo,
        LocalDateTime createAt,
        LocalDateTime updateAt
    ) {
        this.id = id;
        this.nickName = nickName;
        this.sex = sex;
        this.prefectureId = prefectureId;
        this.email = email;
        this.memo = memo;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Long getId() {
        return this.id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNickName() {
        return this.nickName;
    }

    public User setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Sex getSex() {
        return this.sex;
    }

    public User setSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public Prefecture getPrefectureId() {
        return this.prefectureId;
    }

    public User setPrefectureId(Prefecture prefectureId) {
        this.prefectureId = prefectureId;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getMemo() {
        return this.memo;
    }

    public User setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    public User setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public LocalDateTime getUpdateAt() {
        return this.updateAt;
    }

    public User setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (nickName == null) {
            if (other.nickName != null)
                return false;
        }
        else if (!nickName.equals(other.nickName))
            return false;
        if (sex == null) {
            if (other.sex != null)
                return false;
        }
        else if (!sex.equals(other.sex))
            return false;
        if (prefectureId == null) {
            if (other.prefectureId != null)
                return false;
        }
        else if (!prefectureId.equals(other.prefectureId))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        }
        else if (!email.equals(other.email))
            return false;
        if (memo == null) {
            if (other.memo != null)
                return false;
        }
        else if (!memo.equals(other.memo))
            return false;
        if (createAt == null) {
            if (other.createAt != null)
                return false;
        }
        else if (!createAt.equals(other.createAt))
            return false;
        if (updateAt == null) {
            if (other.updateAt != null)
                return false;
        }
        else if (!updateAt.equals(other.updateAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.nickName == null) ? 0 : this.nickName.hashCode());
        result = prime * result + ((this.sex == null) ? 0 : this.sex.hashCode());
        result = prime * result + ((this.prefectureId == null) ? 0 : this.prefectureId.hashCode());
        result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
        result = prime * result + ((this.memo == null) ? 0 : this.memo.hashCode());
        result = prime * result + ((this.createAt == null) ? 0 : this.createAt.hashCode());
        result = prime * result + ((this.updateAt == null) ? 0 : this.updateAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(id);
        sb.append(", ").append(nickName);
        sb.append(", ").append(sex);
        sb.append(", ").append(prefectureId);
        sb.append(", ").append(email);
        sb.append(", ").append(memo);
        sb.append(", ").append(createAt);
        sb.append(", ").append(updateAt);

        sb.append(")");
        return sb.toString();
    }
}