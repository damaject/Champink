package ink.champ.controllers;

import ink.champ.models.Champ;
import ink.champ.models.Role;
import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class MainController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "";
    private String auth;

    @GetMapping("/index")
    public String index(Model model) {
        return "redirect:/";
    }

    @GetMapping("/")
    public String root(Model model) {
        app.updateModel(model, page, "Champink");
        return "index";
    }

    @GetMapping("/auth")
    public String login(@RequestParam(required = false) String action, Model model) {
        if (action != null) {
            switch (action) {
                case "login":
                    app.updateModel(model, page, "Champink - Авторизация");
                    return "auth/login";
                case "registration":
                    app.updateModel(model, page, "Champink - Регистрация");
                    return "auth/registration";
                case "restore":
                    app.updateModel(model, page, "Champink - Восстановление");
                    return "auth/restore";
            }
        }
        return "redirect:/auth?action=login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        app.updateModel(model, page, "Champink - Профиль");
        return "profile";
    }

    @PostMapping("/post/auth/login")
    public String postAuthLogin(@RequestParam String email, @RequestParam String password) {

//        service.addNewChamp(new Champ(name, sport));
        return "redirect:/champs";
    }

    @PostMapping("/post/auth/registration")
    public String postAuthRegistration(@RequestParam String username, @RequestParam String password, @RequestParam String password2) {
        if (!password.equals(password2)) return "redirect:/auth?action=registration";
        User user = new User(username, password);
        user.setRoles(Collections.singleton(Role.USER));
        service.addNewUser(user);
        return "redirect:/auth?action=login";
    }

//    @GetMapping("/sports")
//    public String sports(Model model) {
//        model.addAttribute("sports", service.getSports());
//        return "index";
//    }
//
//    @GetMapping("/users")
//    public String users(Model model) {
//        model.addAttribute("users", service.getUsers());
//        return "index";
//    }


//    @GetMapping("/users/new")
//    public String newuser(Model model) {
//        return "newuser";
//    }

//
//    @GetMapping("users/{id}")
//    public String userById(@PathVariable(value = "id") long id, Model model) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("userCurrent", arrayList);
//        return "currentuser";
//    }

}
