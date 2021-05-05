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

    @GetMapping("/players")
    public String players(@RequestParam(required = false) String subpage, @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/players";
        }
        if (app.subpage == null) app.subpage = AppService.Subpage.GLOBAL;

        if (app.subpage.equals(AppService.Subpage.GLOBAL)) model.addAttribute("players", service.getGlobalPlayers());
        else if (app.subpage.equals(AppService.Subpage.USER_ALL)) model.addAttribute("players", service.getUserPlayersAll(user));
        else if (app.subpage.equals(AppService.Subpage.USER_OWNER)) model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.OWNER));
        else if (app.subpage.equals(AppService.Subpage.USER_MANAGER)) model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.MANAGER));
        else if (app.subpage.equals(AppService.Subpage.USER_VIEWER)) model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.VIEWER));
        else return "redirect:/players?subpage=" + AppService.Subpage.GLOBAL;

        app.updateModel(user, model, page, "Champink - Игроки");
        return "player/list";
    }

    @GetMapping("/player/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Player player = service.getPlayerById(id);
        if (player == null) return "redirect:/players";
        model.addAttribute("player", player);
        app.updateModel(user, model, page, "Champink - Игрок " + player.getName());
        return "player/view";
    }

    @GetMapping("/player/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        app.subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, "Champink - Новый игрок");
        return "player/new";
    }

    @GetMapping("/player/{id}/edit")
    public String playerEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) >= AppService.Role.MANAGER) {
                model.addAttribute("player", player);
                app.updateModel(user, model, page, "Champink - Игрок " + player.getName() + " - Редактирование");
                return "player/edit";
            }
        }
        return "redirect:/players";
    }

    @GetMapping("/player/{id}/roles/set")
    public String playerRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam Integer role) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            PlayerRole playerRole = player.getPlayerRole(user);
            if (playerRole == null) {
                playerRole = new PlayerRole(player, user, AppService.Role.NONE);
                service.addNewPlayerRole(playerRole);
            }
            if ((role == 0 || role == 1) && playerRole.getRole() < AppService.Role.OWNER) {
                playerRole.setRole(role);
                playerRole.setRequest(AppService.Role.NONE);
                service.savePlayerRole(playerRole);
            }
            else if (role == AppService.Role.MANAGER && playerRole.getRole() < AppService.Role.MANAGER) {
                playerRole.setRequest(role);
                service.savePlayerRole(playerRole);
            }
        }
        return "redirect:/player/" + id;
    }

    @GetMapping("/player/{id}/roles/{pr}/{action}")
    public String teamRoleAction(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @PathVariable(value = "pr") Long prId, @PathVariable(value = "action") String action) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) == AppService.Role.OWNER) {
                PlayerRole playerRole = service.getPlayerRoleById(prId);
                if (action.equals("accept")) {
                    playerRole.setRole(playerRole.getRequest());
                    playerRole.setRequest(AppService.Role.NONE);
                }
                else if (action.equals("reject")) playerRole.setRequest(AppService.Role.NONE);
                else if (action.equals("delete")) playerRole.setRole(AppService.Role.VIEWER);
                service.savePlayerRole(playerRole);
            }
        }
        return "redirect:/player/" + id;
    }

    @GetMapping("/player/{id}/delete/{admin}")
    public String playerDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @PathVariable(value = "admin", required = false) Boolean admin) {
        if (user == null) return "redirect:/players";
        Player player = service.getPlayerById(id);
        if ((admin && user.isAdmin()) || (!admin && player.getUserRole(user) == AppService.Role.OWNER)) service.deletePlayer(player);
        if (admin) return "redirect:/admin?subpage=players";
        else return "redirect:/players";
    }

    @PostMapping("/post/player/add")
    public String postChampAdd(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Player newPlayer = new Player(name, privat, user);
            service.addNewPlayer(newPlayer);
            service.addNewPlayerRole(new PlayerRole(newPlayer, user, AppService.Role.OWNER));
        }
        return "redirect:/players?subpage=" + AppService.Subpage.USER_OWNER;
    }

    @PostMapping("/post/player/{id}/edit")
    public String postPlayerEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @RequestParam String name, @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) >= AppService.Role.MANAGER) {
                player.setName(name);
                player.setPrivate(privat);
                service.savePlayer(player);
            }
        }
        return "redirect:/player/" + id;
    }
}