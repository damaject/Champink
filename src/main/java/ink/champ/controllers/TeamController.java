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

        if (this.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("teams", service.getGlobalTeams());
        else if (this.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("teams", service.getUserTeamsAll(user));
        else if (this.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.OWNER));
        else if (this.subpage.equals(AppService.Subpage.USER_VIEWER)) model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.VIEWER));

        app.updateModel(user, model, page, this.subpage, "Champink - Команды");
        return "team/list";
    }

    @GetMapping("/team/{id}")
    public String teamById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Team team = service.getTeamById(id);
        if (team == null) return "redirect:/teams";
        model.addAttribute("team", team);
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

    @GetMapping("/team/{id}/players/add")
    public String teamPlayersAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if (team.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("team", team);
            model.addAttribute("players", service.getUserPlayersNotInTeam(user, team));
            app.updateModel(user, model, page, subpage, "Champink - Команда " + team.getName() + " - Добавление игрока");
            return "team/add-player";
        }
        return "redirect:/team/" + id;
    }

    @GetMapping("/team/{id}/players/{tp}/delete")
    public String teamPlayersDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                             @PathVariable(value = "tp") Long tpId) {
        if (user == null) return "redirect:/teams";
        TeamPlayer teamPlayer = service.getTeamPlayerById(tpId);
        if (teamPlayer.getTeam().getUserRole(user) >= AppService.Role.MANAGER) service.deleteTeamPlayer(teamPlayer);
        return "redirect:/team/" + id;
    }

    @GetMapping("/team/{id}/delete/{admin}")
    public String teamDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @PathVariable(value = "admin", required = false) Boolean admin) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if ((admin && user.isAdmin()) || (!admin && team.getUserRole(user) == AppService.Role.OWNER)) service.deleteTeam(team);
        if (admin) return "redirect:/admin?subpage=teams";
        else return "redirect:/teams";
    }

    @PostMapping("/post/team/add")
    public String postTeamAdd(@AuthenticationPrincipal User user, @RequestParam String name,
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
