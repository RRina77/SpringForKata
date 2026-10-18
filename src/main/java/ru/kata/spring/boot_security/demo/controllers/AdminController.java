package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User currentUser,
                            Model model) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.listUsers());
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.listRoles());
        model.addAttribute("selectedRoleIds", Collections.emptySet());
        model.addAttribute("editing", false);
        return "user-form";
    }

    @PostMapping
    public String createUser(
            @ModelAttribute("user") User user,
            @RequestParam(value = "roleIds", required = false)
            Set<Integer> roleIds,
            Model model) {

        try {
            userService.createUser(user, roleIds);
            return "redirect:/admin";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("roles", userService.listRoles());
            model.addAttribute(
                    "selectedRoleIds",
                    roleIds == null ? Collections.emptySet() : roleIds
            );
            model.addAttribute("editing", false);
            model.addAttribute("error", exception.getMessage());
            return "user-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable int id, Model model) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
        model.addAttribute("userId", id);
        model.addAttribute("roles", userService.listRoles());
        model.addAttribute(
                "selectedRoleIds",
                user.getRoles().stream()
                        .map(role -> role.getId())
                        .collect(Collectors.toSet())
        );
        model.addAttribute("editing", true);

        return "user-form";
    }

    @PostMapping("/{id}")
    public String updateUser(
            @PathVariable int id,
            @ModelAttribute("user") User user,
            @RequestParam(value = "roleIds", required = false)
            Set<Integer> roleIds,
            Model model) {

        try {
            userService.updateUser(id, user, roleIds);
            return "redirect:/admin";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("userId", id);
            model.addAttribute("roles", userService.listRoles());
            model.addAttribute(
                    "selectedRoleIds",
                    roleIds == null ? Collections.emptySet() : roleIds
            );
            model.addAttribute("editing", true);
            model.addAttribute("error", exception.getMessage());
            return "user-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}