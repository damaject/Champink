package ink.champ.controllers;

import ink.champ.models.Champ;
import ink.champ.models.Sport;
import ink.champ.models.User;
import ink.champ.repository.ChampRepository;
import ink.champ.repository.SportRepository;
import ink.champ.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired private ChampRepository repChamps;
    @Autowired private SportRepository repSports;
    @Autowired private UserRepository repUsers;

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/champs")
    public String champs(Model model) {
        Iterable<Champ> champs = repChamps.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("champs", champs);
        return "champs";
    }

    @GetMapping("/sports")
    public String sports(Model model) {
        Iterable<Sport> sports = repSports.findAll();
        model.addAttribute("sports", sports);
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        Iterable<User> users = repUsers.findAll();
        model.addAttribute("users", users);
        return "index";
    }


//    @GetMapping("/users/new")
//    public String newuser(Model model) {
//        return "newuser";
//    }
//
//    @PostMapping("users/new")
//    public String newUserAdd(@RequestParam String name, @RequestParam int age, @RequestParam String email) {
//        Users user = new Users(name, email, age);
//        usersRepository.save(user);
//        return "redirect:/users";
//    }
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
