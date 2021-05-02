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

    private final String page = "admin";
    private String subpage;

    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (user == null || !user.isAdmin()) return "redirect:/login";
        if (subpage != null) {
            this.subpage = subpage;
            return "redirect:/admin";
        }
        if (this.subpage == null || this.subpage.equals("")) this.subpage = "users";

        app.updateModel(user, model, page, this.subpage, "Champink - Администратор");
        if (this.subpage.equals("users")) {
            model.addAttribute("users", service.getUsers());
            return "admin/users";
        }
        else if (this.subpage.equals("sports")) {
            model.addAttribute("sports", service.getSports());
            return "admin/sports";
        }
        else if (this.subpage.equals("champs")) {
            model.addAttribute("champs", service.getChamps());
            return "admin/champs";
        }
        else if (this.subpage.equals("teams")) {
            model.addAttribute("teams", service.getTeams());
            return "admin/teams";
        }
        else if (this.subpage.equals("players")) {
            model.addAttribute("players", service.getPlayers());
            return "admin/players";
        }
        return "admin/index";
    }
}
