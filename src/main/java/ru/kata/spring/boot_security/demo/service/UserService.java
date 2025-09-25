package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    void saveUser(User user);

    void deleteUser(long id);

    void updateUser(long id, User user, String[] selectedRoles);

    void setUserRoles(User user, String[] selectedRoles);

//    void updateUserWithRoles(long id, String name, String lastName, byte age,
//                             String username, String password, String[] selectedRoles);
}
