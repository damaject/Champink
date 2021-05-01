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
public class ChampController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "champ";
    private String subpage;

    @GetMapping("/champs")
    public String champs(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            this.subpage = subpage;
            return "redirect:/champs";
        }
        if (this.subpage == null) this.subpage = AppService.Subpage.GLOBAL;

        if (this.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("champs", service.getChamps());
        else if (this.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("champs", service.getUserChamps(user));
        else if (this.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("champs", service.getUserChamps(user));

        app.updateModel(user, model, page, this.subpage, "Champink - Чемпионаты");
        return "champ/list";
    }

    @GetMapping("/champ/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        model.addAttribute("champ", champ);
        model.addAttribute("user_teams", service.getUserTeams(user));
        app.updateModel(user, model, page, subpage, "Champink - Чемпионат " + champ.getName());
        return "champ/view";
    }

    @GetMapping("/champ/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("sports", service.getSports());
        subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, subpage, "Champink - Новый чемпионат");
        return "champ/new";
    }

    @GetMapping("/champ/{id}/roles/set")
    public String champRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Integer role) {
        if (user != null && (role == 0 || role == 1)) {
            Champ champ = service.getChampById(id);
            ChampRole champRole = champ.getChampRole(user);
            if (champRole == null || champRole.getRole() <= AppService.Role.VIEWER) {
                if (champRole == null) service.addNewChampRole(new ChampRole(champ, user, role));
                else {
                    champRole.setRole(role);
                    service.saveChampRole(champRole);
                }
            }
        }
        return "redirect:/champ/" + id;
    }

    @PostMapping("/post/champ/new")
    public String postChampNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat, @RequestParam Long sport) {
        if (user != null) {
            Champ newChamp = new Champ(name, privat, service.getSportById(sport), user);
            service.addNewChamp(newChamp);
            service.addNewChampRole(new ChampRole(newChamp, user, AppService.Role.OWNER));
        }
        return "redirect:/champs?subpage=" + AppService.Subpage.USER_OWNER;
    }

    @PostMapping("/post/champ/{id}/teams/add")
    public String postTeamPlayerAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Team team) {
        if (user != null) service.addNewChampTeam(new ChampTeam(service.getChampById(id), team));
        return "redirect:/champ/" + id;
    }
}
