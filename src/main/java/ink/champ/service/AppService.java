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

    public class Subpage {
        public static final String GLOBAL = "global";
        public static final String USER_ALL = "u-all";
        public static final String USER_OWNER = "u-owner";
        public static final String USER_VIEWER = "u-viewer";
    }

    public class Role {
//        public static final int REQUEST_MANAGER = -3;
//        public static final int REQUEST_JUDGE = -2;
        public static final int NONE = 0;
        public static final int VIEWER = 1;
        public static final int JUDGE = 2;
        public static final int MANAGER = 3;
        public static final int OWNER = 4;
    }

    @Autowired private RepositoryService service;

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return service.getUserByUsername(username);
    }

    public void updateModel(User user, Model model, String page, String subpage, String title) {
        String name = user != null ? user.getName() : "Гость";

        model.addAttribute("page", page);
        model.addAttribute("subpage", subpage);
        model.addAttribute("title", title);
        model.addAttribute("user", user);
        model.addAttribute("name", name);
    }

}
