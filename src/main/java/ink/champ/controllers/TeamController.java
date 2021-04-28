package ink.champ.controllers;

import ink.champ.models.Champ;
import ink.champ.models.Team;
import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TeamController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "team";

    @GetMapping("/teams")
    public String teams(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("teams", service.getTeams());
        app.updateModel(user, model, page, "", "Champink - Команды");
        return "team/list";
    }

    @GetMapping("/team/{id}")
    public String teamById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Team team = service.getTeamById(id);
        model.addAttribute("team", team);
        app.updateModel(user, model, page, "", "Champink - Команда " + team.getName());
        return "team/view";
    }

    @GetMapping("/team/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, page, "", "Champink - Новая команда");
        return "team/new";
    }

    @PostMapping("/post/team/new")
    public String postChampNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) service.addNewTeam(new Team(name, privat, user));
        return "redirect:/teams";
    }
}
