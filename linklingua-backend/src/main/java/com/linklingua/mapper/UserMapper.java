package com.linklingua.mapper;

import com.linklingua.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * User Mapper interface (annotation-based SQL).
 *
 * <p>Note: {@code user} is a reserved word in MySQL, so the table name is wrapped in backticks.</p>
 *
 * @author LinkLingua
 */
@Mapper
public interface UserMapper {

    /**
     * Inserts a new user and writes back the generated id.
     */
    @Insert("INSERT INTO `user` (username, password, create_time) VALUES (#{username}, #{password}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * Updates a user (username and password) by id.
     */
    @Update("UPDATE `user` SET username = #{username}, password = #{password} WHERE id = #{id}")
    int updateById(User user);

    /**
     * Deletes a user by id.
     */
    @Delete("DELETE FROM `user` WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * Selects a user by id.
     */
    @Select("SELECT id, username, password, create_time FROM `user` WHERE id = #{id}")
    User selectById(Integer id);

    /**
     * Selects a user by username (used for uniqueness checks and login).
     */
    @Select("SELECT id, username, password, create_time FROM `user` WHERE username = #{username}")
    User selectByUsername(String username);

    /**
     * Selects all users.
     */
    @Select("SELECT id, username, password, create_time FROM `user` ORDER BY id")
    List<User> selectAll();
}
