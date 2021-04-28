package ink.champ.controllers;

import ink.champ.models.Role;
import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;

@Controller
public class AuthController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "auth";

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, page, "", "Champink - Авторизация");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, page, "", "Champink - Регистрация");
        return "auth/registration";
    }

    @GetMapping("/restore")
    public String restore(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, page, "", "Champink - Восстановление");
        return "auth/restore";
    }

    @PostMapping("/post/auth/registration")
    public String postAuthRegistration(@AuthenticationPrincipal User user, @RequestParam String username,
                                       @RequestParam String password, @RequestParam String password2,
                                       @RequestParam String name, @RequestParam String email) {
        if (!password.equals(password2)) return "redirect:/auth?action=registration";
        User newUser = new User(username, password, name, email);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        newUser.setRoles(roles);
        service.addNewUser(newUser);
        return "redirect:/auth?action=login";
    }
}
