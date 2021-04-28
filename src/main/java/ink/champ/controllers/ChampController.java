package ink.champ.controllers;

import ink.champ.models.Champ;
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
        if (this.subpage == null) this.subpage = "global";
        model.addAttribute("champs", service.getChamps());
        app.updateModel(user, model, page, this.subpage, "Champink - Чемпионаты");
        return "champ/list";
    }

    @GetMapping("/champ/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        model.addAttribute("champ", champ);
        app.updateModel(user, model, page, "", "Champink - Чемпионат " + champ.getName());
        return "champ/view";
    }

    @GetMapping("/champ/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("sports", service.getSports());
        subpage = "u-owner";
        app.updateModel(user, model, page, subpage, "Champink - Новый чемпионат");
        return "champ/new";
    }

    @PostMapping("/post/champ/new")
    public String postChampNew(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat, @RequestParam Long sport) {
        if (user != null) service.addNewChamp(new Champ(name, privat, service.getSportById(sport), user));
        return "redirect:/champs?subpage=u-owner";
    }
}
