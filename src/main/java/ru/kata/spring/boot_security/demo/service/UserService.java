package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    void saveUser(User user, String[] newRoles);

    void deleteUser(long id);

    void setUserRoles(User user, String[] selectedRoles);

    void updateUser(long id, User user, String[] selectedRoles);

}
