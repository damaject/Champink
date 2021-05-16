package ink.champ.controllers;

import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Класс-контроллер для обработки запросов раздела администратора
 * @author Maxim
 */
@Controller
public class AdminController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    /**
     * Метод для просмотра пользователей, видов спорта, чемпионатов, команд и игроков с правами администратора
     * @param subpage Раздел страницы
     * @param search Текст для поиска
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String subpage, @RequestParam(required = false) String search,
                        @AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/admin";
        }
        if (app.subpage == null || app.subpage.equals("")) app.subpage = "users";

        String s = "";
        String title = "Администратор";
        String hTitle = "";
        if (search != null && !search.isEmpty()) {
            s = search;
            hTitle = " - поиск «" + search + "»";
            title += hTitle;
        }

        model.addAttribute("headerTitle", hTitle);
        app.updateModel(user, model, "admin", title);
        switch (app.subpage) {
            case "users":
                model.addAttribute("users", service.getUsers(s));
                return "admin/users";
            case "sports":
                model.addAttribute("sports", service.getSports(s));
                return "admin/sports";
            case "champs":
                model.addAttribute("champs", service.getChamps(s));
                return "admin/champs";
            case "teams":
                model.addAttribute("teams", service.getTeams(s));
                return "admin/teams";
            case "players":
                model.addAttribute("players", service.getPlayers(s));
                return "admin/players";
        }

        return "redirect:/admin?subpage=users";
    }

}
