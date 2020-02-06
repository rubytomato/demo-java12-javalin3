package com.example.demo.service;

import com.example.demo.model.tables.pojos.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> findById(Long id);
  List<User> findByNickName(String nickName);
  List<User> findAll();
  User save(User user);
  void remove(Long id);
}
