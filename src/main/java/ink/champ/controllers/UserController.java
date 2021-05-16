package ink.champ.controllers;

import ink.champ.models.Role;
import ink.champ.models.User;
import ink.champ.service.AppService;
import ink.champ.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

/**
 * Класс-контроллер для обработки запросов раздела пользователей
 * @author Maxim
 */
@Controller
public class UserController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private String preLoginUrl;

    /**
     * Метод для отображения страницы авторизации пользователя
     * @param user Пользователь
     * @param model Модель
     * @param request HTTP-запрос
     * @return Шаблон
     */
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user, Model model, HttpServletRequest request) {
        app.updateModel(user, model, "auth", "Авторизация");
        preLoginUrl = request.getHeader("Referer");
        return "auth/login";
    }

    /**
     * Метод для отображения страницы регистрации пользователя
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/registration")
    public String registration(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "auth", "Регистрация");
        return "auth/registration";
    }

    /**
     * Метод для отображения страницы восстановления пользователя
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/restore")
    public String restore(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "auth", "Восстановление");
        return "auth/restore";
    }

    /**
     * Метод для отображения страницы профиля
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "profile", "Профиль");
        model.addAttribute("user", user);
        return "user/view";
    }

    /**
     * Метод для отображения страницы редактирования профиля
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/profile/edit")
    public String profileEdit(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "profile", "Профиль - редактирование");
        model.addAttribute("user", user);
        return "user/edit";
    }

    /**
     * Метод для отображения страницы изменения пароля
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/profile/password")
    public String profilePassword(@AuthenticationPrincipal User user, Model model) {
        app.updateModel(user, model, "profile", "Профиль - изменение пароля");
        model.addAttribute("user", user);
        return "user/password";
    }

    /**
     * Метод для переадресации после успешной авторизации
     * @return Переадресация
     */
    @GetMapping("/login/success")
    public String loginSuccess() {
        if (preLoginUrl == null || preLoginUrl.equals("") || preLoginUrl.contains("/login") ||
                preLoginUrl.contains("/registration") || preLoginUrl.contains("/restore") || preLoginUrl.contains("/profile")) preLoginUrl = "/";
        return "redirect:" + preLoginUrl;
    }

    /**
     * Метод для удаления пользователя
     * @param user Пользователь
     * @param id Идентификатор пользователя
     * @return Переадресация
     */
    @GetMapping("/user/{id}/delete")
    public String userDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id) {
        if (user == null) return "redirect:/";
        User deleteUser = service.getUserById(id);
        if (user.isAdmin() && !deleteUser.isAdmin()) service.deleteUser(deleteUser);
        return "redirect:/admin?subpage=users";
    }

    /**
     * Метод для изменения активности пользователя
     * @param user Пользователь
     * @param id Идентификатор пользователя
     * @param active Активность
     * @return Переадресация
     */
    @GetMapping("/user/{id}/active/{active}")
    public String userActive(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                             @PathVariable(value = "active") Long active) {
        if (user == null) return "redirect:/";
        User editUser = service.getUserById(id);
        if (user.isAdmin() && !editUser.isAdmin()) {
            editUser.setActive(active == 1);
            service.saveUser(editUser);
        }
        return "redirect:/admin?subpage=users";
    }

    /**
     * Метод для редактирования пользователя
     * @param user Пользователь
     * @param id Идентификатор пользователя
     * @param name Имя
     * @param email Адрес электронной почты
     * @return Переадресация
     */
    @PostMapping("/post/user/{id}/edit")
    public String postUserEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @RequestParam String name, @RequestParam String email) {
        if (user == null || !user.getId().equals(id)) return "redirect:/";
        user.setName(name);
        user.setEmail(email);
        service.saveUser(user);
        return "redirect:/profile";
    }

    /**
     *
     * @param user Пользователь
     * @param id Идентификатор пользователя
     * @param password1 Старый пароль
     * @param password2 Новый пароль
     * @param password3 Подтверждение нового пароля
     * @return Переадресация
     */
    @PostMapping("/post/user/{id}/password")
    public String postUserPassword(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @RequestParam String password1, @RequestParam String password2, @RequestParam String password3) {
        if (user == null || !user.getId().equals(id)) return "redirect:/";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!password2.equals(password3) || !encoder.matches(password1, user.getPassword())) return "redirect:/profile/password?error";
        user.setPassword(encoder.encode(password2));
        service.saveUser(user);
        return "redirect:/profile";
    }

    /**
     * Метод для регистрации пользователя
     * @param username Логин
     * @param password Пароль
     * @param password2 Подтверждение пароля
     * @param name Имя
     * @param email Адрес электронной почты
     * @return Переадресация
     */
    @PostMapping("/post/auth/registration")
    public String postAuthRegistration(@RequestParam String username, @RequestParam String password,
                                       @RequestParam String password2, @RequestParam String name, @RequestParam String email) {
        if (!password.equals(password2)) return "redirect:/auth?action=registration";
        User newUser = new User(username, password, name, email);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);
//        roles.add(Role.ADMIN);
        newUser.setRoles(roles);
        service.addNewUser(newUser);
        return "redirect:/auth?action=login";
    }

}
