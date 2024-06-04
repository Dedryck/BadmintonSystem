package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(Integer userID);
    boolean validateUser(String username, String password);
    List<User> findAllUsers();
    User findUserById(Integer id);
    int countUsers();
    Integer getUserIDByUsername(String username); // 新增方法

    UserDetails loadUserByUsername(String username); // 新增方法
}
