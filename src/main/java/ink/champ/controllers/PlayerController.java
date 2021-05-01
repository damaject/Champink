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
public class PlayerController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "player";
    private String subpage;

    @GetMapping("/players")
    public String players(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            this.subpage = subpage;
            return "redirect:/players";
        }
        if (this.subpage == null) this.subpage = AppService.Subpage.GLOBAL;

        if (this.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("players", service.getPlayers());
        else if (this.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("players", service.getUserPlayers(user));
        else if (this.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("players", service.getUserPlayers(user));

        app.updateModel(user, model, page, this.subpage, "Champink - Игроки");
        return "player/list";
    }

    @GetMapping("/player/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Player player = service.getPlayerById(id);
        model.addAttribute("player", player);
        app.updateModel(user, model, page, subpage, "Champink - Игрок " + player.getName());
        return "player/view";
    }

    @GetMapping("/player/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, subpage, "Champink - Новый игрок");
        return "player/new";
    }

    @GetMapping("/player/{id}/roles/set")
    public String playerRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Integer role) {
        if (user != null && (role == 0 || role == 1)) {
            Player player = service.getPlayerById(id);
            PlayerRole playerRole = player.getPlayerRole(user);
            if (playerRole == null || playerRole.getRole() <= AppService.Role.VIEWER) {
                if (playerRole == null) service.addNewPlayerRole(new PlayerRole(player, user, role));
                else {
                    playerRole.setRole(role);
                    service.savePlayerRole(playerRole);
                }
            }
        }
        return "redirect:/player/" + id;
    }

    @PostMapping("/post/player/new")
    public String postChampNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Player newPlayer = new Player(name, privat, user);
            service.addNewPlayer(newPlayer);
            service.addNewPlayerRole(new PlayerRole(newPlayer, user, AppService.Role.OWNER));
        }
        return "redirect:/players?subpage=" + AppService.Subpage.USER_OWNER;
    }
}
