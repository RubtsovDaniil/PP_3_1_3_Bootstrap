package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getUserAddForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin/adminPanel";
    }

    @GetMapping("/updateUser")
    public String getUserUpdateForm(@RequestParam(value = "editUserId") Long editUserId, Model model) {
        model.addAttribute("existingUser", userService.getUserById(editUserId));
        return "admin/adminPanel";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam(value = "newRoles") String[] newRoles) {
        userService.saveUser(user, newRoles);
        return "redirect:/admin";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("userId") long id,
                             @ModelAttribute("existingUser") User user,
                             @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
        userService.updateUser(id, user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}