package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserService userService,
                           RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleService.getRoleByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleService.saveRole(adminRole);
        }

        Role userRole = roleService.getRoleByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleService.saveRole(userRole);
        }

        Optional<User> adminUserOpt = userRepository.findByUsername("admin@mail.ru");
        if (adminUserOpt.isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin@mail.ru");
            adminUser.setPassword("admin");
            adminUser.setName("Alex");
            adminUser.setLastName("Smith");
            adminUser.setAge((byte) 12);
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            String[] roleNames = adminRoles.stream()
                    .map(Role::getName)
                    .toArray(String[]::new);
            userService.saveUser(adminUser, roleNames);
        }
        Optional<User> regularUserOpt = userRepository.findByUsername("user@mail.ru");
        if (regularUserOpt.isEmpty()) {
            User regularUser = new User();
            regularUser.setUsername("user@mail.ru");
            regularUser.setPassword("admin");
            regularUser.setName("Adam");
            regularUser.setLastName("Black");
            regularUser.setAge((byte) 60);
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            String[] roleNames = userRoles.stream()
                    .map(Role::getName)
                    .toArray(String[]::new);
            userService.saveUser(regularUser, roleNames);
        }
    }
}
