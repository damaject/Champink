package ink.champ.service;

import ink.champ.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AppService implements UserDetailsService {

    @Autowired private RepositoryService service;

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return service.getUserByUsername(username);
    }

    public void updateModel(User user, Model model, String page, String subpage, String title) {
        System.out.println(title + " " + page + " " + subpage);
        String name = user != null ? user.getName() : "Гость";

        model.addAttribute("page", page);
        model.addAttribute("subpage", subpage);
        model.addAttribute("title", title);
        model.addAttribute("name", name);
    }

}
