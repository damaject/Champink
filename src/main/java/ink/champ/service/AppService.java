package ink.champ.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AppService {

    private enum Role { Guest, User, Admin }

    private Role userRole;
    private String userName = "Test";
    private int userId = -1;

    public int getUserId() { return userId; }
    public String getUserName() { return userName; }

    public void updateModel(Model model, String page, String title) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        if (userName.equals("anonymousUser")) userName = "Гость";

        checkUserAuth();
        model.addAttribute("page", page);
        model.addAttribute("title", title);
        model.addAttribute("userName", userName);
    }

    public void checkUserAuth() {
        userRole = Role.Guest;
        userName = "Гость";
        userId = 0;
    }
}
