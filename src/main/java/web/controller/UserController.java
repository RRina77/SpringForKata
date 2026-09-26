package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import org.springframework.ui.Model;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping //получить пользователей браузер
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/edit")
    public String editUser(Model model, @RequestParam("id") int id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users";
    }

    @PostMapping("/update")
    public String updateUser(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("email") String email) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        userService.updateUser(id, user);

        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(Model model, @RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
