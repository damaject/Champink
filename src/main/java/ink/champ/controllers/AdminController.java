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
public class AdminController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "admin";

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        app.updateModel(user, model, page, "", "Champink - Администратор");
        return "admin/index";
    }

    @GetMapping("/admin/users")
    public String adminUsers(@AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        app.updateModel(user, model, page, "", "Champink - Администратор");
        model.addAttribute("users", service.getUsers());
        return "admin/users";
    }

    @GetMapping("/admin/champs")
    public String adminChamps(@AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        app.updateModel(user, model, page, "", "Champink - Администратор");
        model.addAttribute("champs", service.getChamps());
        return "admin/champs";
    }

    @GetMapping("/admin/teams")
    public String adminTeams(@AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        app.updateModel(user, model, page, "", "Champink - Администратор");
        model.addAttribute("teams", service.getTeams());
        return "admin/teams";
    }

    @GetMapping("/admin/players")
    public String adminPlayers(@AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        app.updateModel(user, model, page, "", "Champink - Администратор");
        model.addAttribute("players", service.getPlayers());
        return "admin/players";
    }
}
