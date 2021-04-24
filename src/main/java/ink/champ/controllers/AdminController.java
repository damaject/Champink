package ink.champ.controllers;

import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "";
    private String auth;

    @GetMapping("/admin")
    public String root(Model model) {
        model.addAttribute("users", service.getUsers());
        app.updateModel(model, page, "Champink - Администратор");
        return "admin";
    }
}
