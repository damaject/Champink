package ink.champ.controllers;

import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "team";

    @GetMapping("/teams")
    public String teams(Model model) {
//        model.addAttribute("champs", service.getChamps());
        app.updateModel(model, page, "Champink - Команды");
        return "team/list";
    }

}
