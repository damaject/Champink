package ink.champ.controllers;

import ink.champ.models.*;
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
    private String subpage;

    @GetMapping("/teams")
    public String teams(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            this.subpage = subpage;
            return "redirect:/teams";
        }
        if (this.subpage == null) this.subpage = AppService.Subpage.GLOBAL;

        if (this.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("teams", service.getTeams());
        else if (this.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("teams", service.getUserTeams(user));
        else if (this.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("teams", service.getUserTeams(user));

        app.updateModel(user, model, page, this.subpage, "Champink - Команды");
        return "team/list";
    }

    @GetMapping("/team/{id}")
    public String teamById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Team team = service.getTeamById(id);
        model.addAttribute("team", team);
        model.addAttribute("user_players", service.getUserPlayers(user));
        app.updateModel(user, model, page, subpage, "Champink - Команда " + team.getName());
        return "team/view";
    }

    @GetMapping("/team/new")
    public String teamNew(@AuthenticationPrincipal User user, Model model) {
        subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, subpage, "Champink - Новая команда");
        return "team/new";
    }

    @GetMapping("/team/{id}/roles/set")
    public String teamRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Integer role) {
        if (user != null && (role == 0 || role == 1)) {
            Team team = service.getTeamById(id);
            TeamRole teamRole = team.getTeamRole(user);
            if (teamRole == null || teamRole.getRole() <= AppService.Role.VIEWER) {
                if (teamRole == null) service.addNewTeamRole(new TeamRole(team, user, role));
                else {
                    teamRole.setRole(role);
                    service.saveTeamRole(teamRole);
                }
            }
        }
        return "redirect:/team/" + id;
    }

    @PostMapping("/post/team/new")
    public String postTeamNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Team newTeam = new Team(name, privat, user);
            service.addNewTeam(newTeam);
            service.addNewTeamRole(new TeamRole(newTeam, user, AppService.Role.OWNER));
        }
        return "redirect:/teams?subpage=" + AppService.Subpage.USER_OWNER;
    }

    @PostMapping("/post/team/{id}/players/add")
    public String postTeamPlayerAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Player player, @RequestParam String position) {
        if (user != null) service.addNewTeamPlayer(new TeamPlayer(service.getTeamById(id), player, position));
        return "redirect:/team/" + id;
    }
}
