package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT UserID, Username, Password, Email, Phone, Role FROM users WHERE Username = #{username}")
    @Results({
            @Result(property = "userID", column = "UserID"),
            @Result(property = "username", column = "Username"),
            @Result(property = "password", column = "Password"),
            @Result(property = "email", column = "Email"),
            @Result(property = "phone", column = "Phone"),
            @Result(property = "role", column = "Role")
    })
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO users (Username, Password, Email, Phone, Role) VALUES (#{username}, #{password}, #{email}, #{phone}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "UserID")
    void insertUser(User user);

    @Update("UPDATE users SET Password = #{password}, Email = #{email}, Phone = #{phone}, Role = #{role} WHERE UserID = #{userID}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE UserID = #{userID}")
    void deleteUser(@Param("userID") Integer userID);

    @Select("SELECT COUNT(*) FROM users")
    int countUsers();

    @Select("SELECT UserID FROM users WHERE Username = #{username}")
    Integer findUserIDByUsername(@Param("username") String username);


    @Select("SELECT * FROM users")
    List<User> findAllUsers();

    @Select("SELECT * FROM users WHERE UserID = #{id}")
    User findUserById(@Param("id") Integer id);
}
