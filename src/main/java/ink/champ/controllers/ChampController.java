package ink.champ.controllers;

import ink.champ.models.*;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

        if (this.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("champs", service.getGlobalChamps());
        else if (this.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("champs", service.getUserChampsAll(user));
        else if (this.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.OWNER));
        else if (this.subpage.equals(AppService.Subpage.USER_VIEWER)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.VIEWER));

        app.updateModel(user, model, page, this.subpage, "Champink - Чемпионаты");
        return "champ/list";
    }

    @GetMapping("/champ/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        if (champ == null) return "redirect:/champs";
        model.addAttribute("champ", champ);
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

    @GetMapping("/champ/{id}/teams/add")
    public String champTeamsAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/champs";
        Champ champ = service.getChampById(id);
        if (champ.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("champ", champ);
            model.addAttribute("teams", service.getUserTeamsNotInChamp(user, champ));
            app.updateModel(user, model, page, subpage, "Champink - Чемпионат " + champ.getName() + " - Добавление команды");
            return "champ/add-team";
        }
        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/events/add")
    public String champEventsAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/champs";
        Champ champ = service.getChampById(id);
        if (champ.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("champ", champ);
            app.updateModel(user, model, page, subpage, "Champink - Чемпионат " + champ.getName() + " - Добавление события");
            return "champ/add-event";
        }
        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/teams/{ct}/delete")
    public String champTeamsDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @PathVariable(value = "ct") Long ctId) {
        if (user == null) return "redirect:/champs";
        ChampTeam champTeam = service.getChampTeamById(ctId);
        if (champTeam.getChamp().getUserRole(user) >= AppService.Role.MANAGER) service.deleteChampTeam(champTeam);
        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/events/{ce}/delete")
    public String champEventsDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                   @PathVariable(value = "ce") Long ceId) {
        if (user == null) return "redirect:/champs";
        ChampEvent champEvent = service.getChampEventById(ceId);
        if (champEvent.getChamp().getUserRole(user) >= AppService.Role.MANAGER) service.deleteChampEvent(champEvent);
        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/delete/{admin}")
    public String champDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                             @PathVariable(value = "admin", required = false) Boolean admin) {
        if (user == null) return "redirect:/champs";
        Champ champ = service.getChampById(id);
        if ((admin && user.isAdmin()) || (!admin && champ.getUserRole(user) == AppService.Role.OWNER)) service.deleteChamp(champ);
        if (admin) return "redirect:/admin?subpage=champs";
        else return "redirect:/champs";
    }

    @PostMapping("/post/champ/add")
    public String postChampAdd(@AuthenticationPrincipal User user, @RequestParam String name,
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

    @PostMapping("/post/champ/{id}/events/add")
    public String postTeamEventAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp,
                                   @RequestParam Team team1, @RequestParam Team team2) {
        if (user != null && !team1.getId().equals(team2.getId())) service.addNewChampEvent(new ChampEvent(service.getChampById(id), team1, team2, Date.from(timestamp.toInstant(ZoneOffset.ofHours(3)))));
        return "redirect:/champ/" + id;
    }
}
