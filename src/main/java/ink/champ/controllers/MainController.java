package ink.champ.controllers;

import ink.champ.models.User;
import ink.champ.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Класс-контроллер для обработки запросов начального раздела и ошибок
 * @author Maxim
 */
@Controller
public class MainController implements ErrorController {

    @Autowired private AppService app;

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String root(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "index", "");
        return "index";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "about", "О сервисе");
        return "about";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
