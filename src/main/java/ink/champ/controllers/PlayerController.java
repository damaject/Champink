package ink.champ.controllers;

import ink.champ.models.Player;
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

import java.util.Random;

@Controller
public class PlayerController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "player";

    @GetMapping("/players")
    public String players(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("players", service.getPlayers());
        app.updateModel(user, model, page, "", "Champink - Игроки");
        return "player/list";
    }

    @GetMapping("/player/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Player player = service.getPlayerById(id);
        model.addAttribute("player", player);
        app.updateModel(user, model, page, "", "Champink - Игрок " + player.getName());
        return "player/view";
    }

    @GetMapping("/player/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, page, "", "Champink - Новый игрок");
        return "player/new";
    }

    @PostMapping("/post/player/new")
    public String postChampNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) service.addNewPlayer(new Player(name, privat, user));
        return "redirect:/players";
    }
}
