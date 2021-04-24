package ink.champ.controllers;

import ink.champ.models.Champ;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/champs")
    public String champs(Model model) {
        model.addAttribute("champs", service.getChamps());
        app.updateModel(model, page, "Champink - Чемпионаты");
        return "champ/list";
    }

    @GetMapping("/champ/{id}")
    public String champById(@PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        model.addAttribute("champ", champ);
        app.updateModel(model, page, "Champink - Чемпионат " + champ.getName());
        return "champ/view";
    }

    @GetMapping("/champ/new")
    public String champNew(Model model) {
        model.addAttribute("sports", service.getSports());
        app.updateModel(model, page, "Champink - Новый чемпионат");
        return "champ/new";
    }

    @PostMapping("/post/champ/new")
    public String postChampNew(@RequestParam String name, @RequestParam Long sport) {
        service.addNewChamp(new Champ(name, sport));
        return "redirect:/champs";
    }
}
