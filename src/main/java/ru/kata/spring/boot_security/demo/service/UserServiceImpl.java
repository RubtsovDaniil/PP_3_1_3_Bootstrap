package ru.kata.spring.boot_security.demo.service;


import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] newRoles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setUserRoles(user, newRoles);
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        getUserById(id);
        userDao.deleteUser(id);
    }

    @Override
    public void setUserRoles(User user, String[] selectedRoles) {
        if (selectedRoles != null) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : selectedRoles) {
                roleSet.add(roleService.getRoleByName(roleName));
            }
            user.setRoles(roleSet);
        }
    }

    @Transactional
    @Override
    public void updateUser(long id, User user, String[] selectedRoles) {
        user.setId(id);
        User existingUser = getUserById(user.getId());
        existingUser.setName(user.getName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setUsername(user.getUsername());
        // Обновляем пароль только если он не пустой
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        setUserRoles(existingUser, selectedRoles);
        userDao.updateUser(existingUser);
    }
}
