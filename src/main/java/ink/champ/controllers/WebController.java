package ink.champ.controllers;

import ink.champ.models.Champ;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @Autowired
    private RepositoryService service;

    @GetMapping("/index")
    public String index(Model model) {
        return "redirect:/";
    }

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("title", "Champink");
        return "index";
    }

    @GetMapping("/champs")
    public String champs(Model model) {
        model.addAttribute("title", "Champink - Чемпионаты");
        model.addAttribute("champs", service.getChamps());
        return "champs";
    }

    @GetMapping("/champ/{id}")
    public String champById(@PathVariable(value = "id") Long id, Model model) {
        Champ champ = service.getChampById(id);
        model.addAttribute("title", "Champink - Чемпионат " + champ.getName());
        model.addAttribute("champ", champ);
        return "champ";
    }

    @GetMapping("/sports")
    public String sports(Model model) {
        model.addAttribute("sports", service.getSports());
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", service.getUsers());
        return "index";
    }


//    @GetMapping("/users/new")
//    public String newuser(Model model) {
//        return "newuser";
//    }
//
    @PostMapping("/champs/new")
    public String newUserAdd(@RequestParam String name) {
        service.addNewChamp(new Champ(name, (long) 0));
        return "redirect:/champs";
    }
//
//    @GetMapping("users/{id}")
//    public String userById(@PathVariable(value = "id") long id, Model model) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("userCurrent", arrayList);
//        return "currentuser";
//    }

}
