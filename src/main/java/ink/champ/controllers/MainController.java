package ink.champ.controllers;

import ink.champ.models.User;
import ink.champ.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {// implements ErrorController {

    @Autowired private AppService app;

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal User user, Model model) {
        return "redirect:/";
    }

//    @GetMapping("/error")
//    public String error(@AuthenticationPrincipal User user, Model model) {
//        return "redirect:/";
//    }

    @GetMapping("/")
    public String root(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "index", "Champink");
        return "index";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "about", "Champink - О сервисе");
        return "about";
    }

//    @Override
//    public String getErrorPath() {
//        return null;
//    }
}
