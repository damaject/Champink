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

    /**
     * Метод для переадресации на главную страницу
     * @return Переадресация
     */
    @GetMapping("/index")
    public String index() { return "redirect:/"; }

    /**
     * Метод для переадресации со страницы ошибки на главную страницу
     * @return Переадресация
     */
    @GetMapping("/error")
    public String error() { return "redirect:/"; }

    /**
     * Метод для отображения главной страницы
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/")
    public String root(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "index", "");
        return "index";
    }

    /**
     * Метод для отображения страницы о сервисе
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "about", "О сервисе");
        return "about";
    }

    /**
     * Метод для получения пути страницы ошибки
     * @return Путь страницы ошибки
     */
    @Override
    public String getErrorPath() { return null; }

}
