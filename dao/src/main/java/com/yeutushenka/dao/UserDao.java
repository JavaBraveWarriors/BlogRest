package com.yeutushenka.dao;

import com.yeutushenka.User;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers() throws DataAccessException;

    User getUserByUserId(Long userId) throws DataAccessException;

    User getUserByLogin(String login) throws DataAccessException;

    Long addUser(User user) throws DataAccessException;

    int updateUser(User user) throws DataAccessException;

    int deleteUser(Long userId) throws DataAccessException;

    boolean checkUserByUserId(Long userId);

    boolean checkUserByLogin(String login);

}
