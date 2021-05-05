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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Controller
public class ChampController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "champ";

    @GetMapping("/champs")
    public String champs(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/champs";
        }
        if (app.subpage == null) app.subpage = AppService.Subpage.GLOBAL;

        if (app.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("champs", service.getGlobalChamps());
        else if (app.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("champs", service.getUserChampsAll(user));
        else if (app.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.OWNER));
        else if (app.subpage.equals(AppService.Subpage.USER_JUDGE)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.JUDGE));
        else if (app.subpage.equals(AppService.Subpage.USER_MANAGER)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.MANAGER));
        else if (app.subpage.equals(AppService.Subpage.USER_VIEWER)) model.addAttribute("champs", service.getUserChampsRole(user, AppService.Role.VIEWER));
        else return "redirect:/champs?subpage=" + AppService.Subpage.GLOBAL;

        app.updateModel(user, model, page, "Champink - Чемпионаты");
        return "champ/list";
    }

    @GetMapping("/champ/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        if (champ == null) return "redirect:/champs";
        model.addAttribute("champ", champ);
        app.updateModel(user, model, page, "Champink - Чемпионат " + champ.getName());

        String[] names = new String[champ.getTeamsCount()];
        long[][] stats = new long[names.length][10];

        int i = 0;
        for (ChampTeam team : champ.getTeams()) {
            names[i] = team.getTeam().getName();
            stats[i][0] = team.getTeam().getId();
            stats[i][1] = i;
            i++;
        }

        for (ChampEvent event : champ.getEvents()) {
            long team1Id = event.getTeam1().getId();
            long team2Id = event.getTeam2().getId();
            int id1 = -1;
            int id2 = -1;
            for (i = 0; i < stats.length; i++) {
                if (stats[i][0] == team1Id) id1 = i;
                if (stats[i][0] == team2Id) id2 = i;
            }
            int gol1 = event.getGol1();
            int gol2 = event.getGol2();
            int pen1 = event.getPen1();
            int pen2 = event.getPen2();

            stats[id1][2]++;
            stats[id2][2]++;
            stats[id1][6] += gol1;
            stats[id2][6] += gol2;
            stats[id1][7] += gol2;
            stats[id2][7] += gol1;
            if (gol1 == gol2 && pen1 == pen2) { // Ничья
                stats[id1][4]++;
                stats[id2][4]++;
            }
            else if (gol1 > gol2 || (gol1 == gol2 && pen1 > pen2)) { // Победа 1
                stats[id1][3]++;
                stats[id2][5]++;
            }
            else if (gol1 < gol2 || (gol1 == gol2 && pen1 < pen2)) { // Победа 2
                stats[id1][5]++;
                stats[id2][3]++;
            }
        }

        for (i = 0; i < stats.length; i++) {
            stats[i][8] = stats[i][6] - stats[i][7];
            stats[i][9] = stats[i][3] * 3 + stats[i][4];
        }

        for (i = 0; i < stats.length - 1; i++) {
            for (int j = i + 1; j < stats.length; j++) {
                if (stats[i][9] < stats[j][9]) {
                    for (int k = 0; k < stats[i].length; k++) {
                        long buf = stats[i][k];
                        stats[i][k] = stats[j][k];
                        stats[j][k] = buf;
                    }
                }
            }
        }

        model.addAttribute("tableNames", names);
        model.addAttribute("tableStats", stats);

        return "champ/view";
    }

    @GetMapping("/champ/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("sports", service.getSports());
        app.subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, "Champink - Новый чемпионат");
        return "champ/new";
    }

    @GetMapping("/champ/{id}/edit")
    public String champEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            if (champ != null && champ.getUserRole(user) >= AppService.Role.MANAGER) {
                model.addAttribute("champ", champ);
                model.addAttribute("sports", service.getSports());
                app.updateModel(user, model, page, "Champink - Чемпионат " + champ.getName() + " - Редактирование");
                return "champ/edit";
            }
        }
        return "redirect:/champs";
    }

    @GetMapping("/champ/{id}/roles/set")
    public String champRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam Integer role) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            ChampRole champRole = champ.getChampRole(user);
            if (champRole == null) {
                champRole = new ChampRole(champ, user, AppService.Role.NONE);
                service.addNewChampRole(champRole);
            }
            if ((role == 0 || role == 1) && champRole.getRole() < AppService.Role.OWNER) {
                champRole.setRole(role);
                champRole.setRequest(AppService.Role.NONE);
                service.saveChampRole(champRole);
            }
            else if (role == AppService.Role.JUDGE && champRole.getRole() < AppService.Role.MANAGER && champRole.getRole() != AppService.Role.JUDGE) {
                champRole.setRequest(role);
                service.saveChampRole(champRole);
            }
            else if (role == AppService.Role.MANAGER && champRole.getRole() < AppService.Role.MANAGER) {
                champRole.setRequest(role);
                service.saveChampRole(champRole);
            }
        }

        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/roles/{cr}/{action}")
    public String teamRoleAction(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @PathVariable(value = "cr") Long crId, @PathVariable(value = "action") String action) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            if (champ != null && champ.getUserRole(user) == AppService.Role.OWNER) {
                ChampRole champRole = service.getChampRoleById(crId);
                if (action.equals("accept")) {
                    champRole.setRole(champRole.getRequest());
                    champRole.setRequest(AppService.Role.NONE);
                }
                else if (action.equals("reject")) champRole.setRequest(AppService.Role.NONE);
                else if (action.equals("delete")) champRole.setRole(AppService.Role.VIEWER);
                service.saveChampRole(champRole);
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
            app.updateModel(user, model, page, "Champink - Чемпионат " + champ.getName() + " - Добавление команды");
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
            app.updateModel(user, model, page, "Champink - Чемпионат " + champ.getName() + " - Добавление события");
            return "champ/add-event";
        }
        return "redirect:/champ/" + id;
    }

    @GetMapping("/champ/{id}/events/{ce}/score")
    public String champEventsScore(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                   @PathVariable(value = "ce") Long ceId, Model model) {
        if (user == null) return "redirect:/champs";
        Champ champ = service.getChampById(id);
        if (champ.getUserRole(user) >= AppService.Role.JUDGE) {
            ChampEvent event = service.getChampEventById(ceId);
            if (event != null) {
                model.addAttribute("champ", champ);
                model.addAttribute("event", event);
                app.updateModel(user, model, page, "Champink - Чемпионат " + champ.getName() + " - Изменение счета");
                return "champ/score";
            }
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
    public String postChampAdd(@AuthenticationPrincipal User user, @RequestParam String name, @RequestParam String format,
                               @RequestParam(defaultValue = "false") boolean privat, @RequestParam Long sport) {
        if (user != null) {
            Champ newChamp = new Champ(name, format, privat, service.getSportById(sport), user);
            service.addNewChamp(newChamp);
            service.addNewChampRole(new ChampRole(newChamp, user, AppService.Role.OWNER));
        }
        return "redirect:/champs?subpage=" + AppService.Subpage.USER_OWNER;
    }

    @PostMapping("/post/champ/{id}/teams/add")
    public String postTeamPlayerAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Team team) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            if (champ.getUserRole(user) >= AppService.Role.MANAGER && team.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewChampTeam(new ChampTeam(champ, team));
            }
        }
        return "redirect:/champ/" + id;
    }

    @PostMapping("/post/champ/{id}/events/add")
    public String postTeamEventAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp,
                                   @RequestParam Team team1, @RequestParam Team team2) {
        if (user != null && !team1.getId().equals(team2.getId())) {
            Champ champ = service.getChampById(id);
            if (champ.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewChampEvent(new ChampEvent(champ, team1, team2, Date.from(timestamp.toInstant(ZoneOffset.ofHours(3)))));
            }
        }
        return "redirect:/champ/" + id;
    }

    @PostMapping("/post/champ/{id}/events/{ce}/score")
    public String postTeamEventScore(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                     @PathVariable(value = "ce") Long ceId, @RequestParam int gol1, @RequestParam int gol2,
                                     @RequestParam int pen1, @RequestParam int pen2) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            if (champ.getUserRole(user) >= AppService.Role.JUDGE) {
                ChampEvent event = service.getChampEventById(ceId);
                if (event != null) {
                    event.setScore(gol1, gol2, pen1, pen2);
                    service.saveChampEvent(event);
                }
            }
        }

        return "redirect:/champ/" + id;
    }

    @PostMapping("/post/champ/{id}/edit")
    public String postChampEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam String name,
                                @RequestParam String format, @RequestParam(defaultValue = "false") boolean privat, @RequestParam Long sport) {
        if (user != null) {
            Champ champ = service.getChampById(id);
            if (champ != null && champ.getUserRole(user) >= AppService.Role.MANAGER) {
                champ.setName(name);
                champ.setFormat(format);
                champ.setPrivate(privat);
                champ.setSport(service.getSportById(sport));
                service.saveChamp(champ);
            }
        }
        return "redirect:/champ/" + id;
    }
}
