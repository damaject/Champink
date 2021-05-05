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

@Controller
public class AdminController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/admin";
        }
        if (app.subpage == null || app.subpage.equals("")) app.subpage = "users";

        app.updateModel(user, model, "admin", "Champink - Администратор");
        if (app.subpage.equals("users")) {
            model.addAttribute("users", service.getUsers());
            return "admin/users";
        }
        else if (app.subpage.equals("sports")) {
            model.addAttribute("sports", service.getSports());
            return "admin/sports";
        }
        else if (app.subpage.equals("champs")) {
            model.addAttribute("champs", service.getChamps());
            return "admin/champs";
        }
        else if (app.subpage.equals("teams")) {
            model.addAttribute("teams", service.getTeams());
            return "admin/teams";
        }
        else if (app.subpage.equals("players")) {
            model.addAttribute("players", service.getPlayers());
            return "admin/players";
        }

        return "redirect:/admin?subpage=users";
    }
}
