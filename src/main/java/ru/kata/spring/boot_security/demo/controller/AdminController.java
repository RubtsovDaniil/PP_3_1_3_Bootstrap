package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/adminPanel";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/adminPanel";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "newRoles", required = false) String[] newRoles) {
        userService.setUserRoles(user, newRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("userId") long id,
                             @ModelAttribute User user,
                             @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
        userService.updateUser(id, user, selectedRoles);
        return "redirect:/admin";
    }

//    @PostMapping("/updateUser")
//    public String updateUser(@RequestParam("userId") long id,
//                             @RequestParam("name") String name,
//                             @RequestParam("lastName") String lastName,
//                             @RequestParam("age") byte age,
//                             @RequestParam("username") String username,
//                             @RequestParam(value = "password", required = false) String password,
//                             @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
//        userService.updateUserWithRoles(id, name, lastName, age, username, password, selectedRoles);
//        return "redirect:/admin";
//    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}