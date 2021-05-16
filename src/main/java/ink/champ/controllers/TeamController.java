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

/**
 * Класс-контроллер для обработки запросов раздела команд
 * @author Maxim
 */
@Controller
public class TeamController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "team";

    @GetMapping("/teams")
    public String teams(@RequestParam(required = false) String subpage, @RequestParam(required = false) String search,
                        @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/teams";
        }
        if (app.subpage == null) app.subpage = AppService.Subpage.GLOBAL;

        String s = "";
        String title = "Команды";
        if (search != null && !search.isEmpty()) {
            s = search;
            title += " - поиск «" + search + "»";
        }

        if (app.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("teams", service.getGlobalTeams(s));
        else if (app.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("teams", service.getUserTeamsAll(user, s));
        else if (app.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.OWNER, s));
        else if (app.subpage.equals(AppService.Subpage.USER_MANAGER)) model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.MANAGER, s));
        else if (app.subpage.equals(AppService.Subpage.USER_VIEWER)) model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.VIEWER, s));
        else return "redirect:/teams?subpage=" + AppService.Subpage.GLOBAL;

        model.addAttribute("headerTitle", title);
        app.updateModel(user, model, page, title);
        return "team/list";
    }

    @GetMapping("/team/{id}")
    public String teamById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Team team = service.getTeamById(id);
        if (team == null) return "redirect:/teams";
        model.addAttribute("team", team);
        app.updateModel(user, model, page, "Команда " + team.getName());
        return "team/view";
    }

    @GetMapping("/team/new")
    public String teamNew(@AuthenticationPrincipal User user, Model model) {
        app.subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, "Новая команда");
        return "team/new";
    }

    @GetMapping("/team/{id}/edit")
    public String teamEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) >= AppService.Role.MANAGER) {
                model.addAttribute("team", team);
                app.updateModel(user, model, page, "Команда " + team.getName() + " - редактирование");
                return "team/edit";
            }
        }
        return "redirect:/teams";
    }

    @GetMapping("/team/{id}/roles/set")
    public String teamRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam Integer role) {
        if (user != null) {
            Team team = service.getTeamById(id);
            TeamRole teamRole = team.getTeamRole(user);
            if (teamRole == null) {
                teamRole = new TeamRole(team, user, AppService.Role.NONE);
                service.addNewTeamRole(teamRole);
            }
            if ((role == 0 || role == 1) && teamRole.getRole() < AppService.Role.OWNER) {
                teamRole.setRole(role);
                teamRole.setRequest(AppService.Role.NONE);
                service.saveTeamRole(teamRole);
            }
            else if (role == AppService.Role.MANAGER && teamRole.getRole() < AppService.Role.MANAGER) {
                teamRole.setRequest(role);
                service.saveTeamRole(teamRole);
            }
        }

        return "redirect:/team/" + id;
    }

    @GetMapping("/team/{id}/roles/{tr}/{action}")
    public String teamRoleAction(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @PathVariable(value = "tr") Long trId, @PathVariable(value = "action") String action) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) == AppService.Role.OWNER) {
                TeamRole teamRole = service.getTeamRoleById(trId);
                if (action.equals("accept")) {
                    teamRole.setRole(teamRole.getRequest());
                    teamRole.setRequest(AppService.Role.NONE);
                }
                else if (action.equals("reject")) teamRole.setRequest(AppService.Role.NONE);
                else if (action.equals("delete")) teamRole.setRole(AppService.Role.VIEWER);
                service.saveTeamRole(teamRole);
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
            app.updateModel(user, model, page, "Команда " + team.getName() + " - добавление игрока");
            return "team/add-player";
        }
        return "redirect:/team/" + id;
    }

    @GetMapping("/team/{id}/champs/add")
    public String teamChampsAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if (team.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("team", team);
            model.addAttribute("champs", service.getUserChampsTeamNotIn(user, team));
            app.updateModel(user, model, page, "Команда " + team.getName() + " - добавление в чемпионат");
            return "team/add-champ";
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
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team.getUserRole(user) >= AppService.Role.MANAGER && player.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewTeamPlayer(new TeamPlayer(team, player, position));
            }
        }
        return "redirect:/team/" + id;
    }

    @PostMapping("/post/team/{id}/champs/add")
    public String postTeamChampAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Champ champ) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team.getUserRole(user) >= AppService.Role.MANAGER && champ.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewChampTeam(new ChampTeam(champ, team));
            }
        }
        return "redirect:/team/" + id;
    }

    @PostMapping("/post/team/{id}/edit")
    public String postTeamEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @RequestParam String name, @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) >= AppService.Role.MANAGER) {
                team.setName(name);
                team.setPrivate(privat);
                service.saveTeam(team);
            }
        }
        return "redirect:/team/" + id;
    }
}
