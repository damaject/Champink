package ink.champ.controllers;

import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal User user, Model model) {
        return "redirect:/";
    }

    @GetMapping("/")
    public String root(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "index", "", "Champink");
        return "index";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "profile", "", "Champink - Профиль");
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "about", "", "Champink - О сервисе");
        return "about";
    }

//    @GetMapping("users/{id}")
//    public String userById(@PathVariable(value = "id") long id, Model model) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("userCurrent", arrayList);
//        return "currentuser";
//    }
}
