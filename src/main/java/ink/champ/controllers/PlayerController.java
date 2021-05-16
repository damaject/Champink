package ink.champ.controllers;

import ink.champ.models.*;
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

/**
 * Класс-контроллер для обработки запросов раздела игроков
 * @author Maxim
 */
@Controller
public class PlayerController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "player";

    /**
     * Метод для отображения страницы игроков
     * @param subpage Раздел страницы
     * @param search Часть имени для поиска
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/players")
    public String players(@RequestParam(required = false) String subpage, @RequestParam(required = false) String search,
                          @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/players";
        }
        if (app.subpage == null) app.subpage = AppService.Subpage.GLOBAL;

        String s = "";
        String title = "Игроки";
        if (search != null && !search.isEmpty()) {
            s = search;
            title += " - поиск «" + search + "»";
        }

        switch (app.subpage) {
            case AppService.Subpage.GLOBAL:
                model.addAttribute("players", service.getGlobalPlayers(s));
                break;
            case AppService.Subpage.USER_ALL:
                model.addAttribute("players", service.getUserPlayersAll(user, s));
                break;
            case AppService.Subpage.USER_OWNER:
                model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.OWNER, s));
                break;
            case AppService.Subpage.USER_MANAGER:
                model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.MANAGER, s));
                break;
            case AppService.Subpage.USER_VIEWER:
                model.addAttribute("players", service.getUserPlayersRole(user, AppService.Role.VIEWER, s));
                break;
            default:
                return "redirect:/players?subpage=" + AppService.Subpage.GLOBAL;
        }

        model.addAttribute("headerTitle", title);
        app.updateModel(user, model, page, title);
        return "player/list";
    }

    /**
     * Метод для отображения страницы игрока по идентификатору
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/player/{id}")
    public String champById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Player player = service.getPlayerById(id);
        if (player == null) return "redirect:/players";
        model.addAttribute("player", player);
        app.updateModel(user, model, page, "Игрок " + player.getName());
        return "player/view";
    }

    /**
     * Метод для отображения страницы создания нового игрока
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/player/new")
    public String champNew(@AuthenticationPrincipal User user, Model model) {
        app.subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, "Новый игрок");
        return "player/new";
    }

    /**
     * Метод для отображения страницы редактирования игрока по идентификатору
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/player/{id}/edit")
    public String playerEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) >= AppService.Role.MANAGER) {
                model.addAttribute("player", player);
                app.updateModel(user, model, page, "Игрок " + player.getName() + " - редактирование");
                return "player/edit";
            }
        }
        return "redirect:/players";
    }

    /**
     * Метод для установки роли игрока
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param role Роль
     * @return Переадресация
     */
    @GetMapping("/player/{id}/roles/set")
    public String playerRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam Integer role) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            PlayerRole playerRole = player.getPlayerRole(user);
            if (playerRole == null) {
                playerRole = new PlayerRole(player, user, AppService.Role.NONE);
                service.addNewPlayerRole(playerRole);
            }
            if ((role == 0 || role == 1) && playerRole.getRole() < AppService.Role.OWNER) {
                playerRole.setRole(role);
                playerRole.setRequest(AppService.Role.NONE);
                service.savePlayerRole(playerRole);
            }
            else if (role == AppService.Role.MANAGER && playerRole.getRole() < AppService.Role.MANAGER) {
                playerRole.setRequest(role);
                service.savePlayerRole(playerRole);
            }
        }
        return "redirect:/player/" + id;
    }

    /**
     * Метод для принятия, отклонения или удаления роли игрока
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param prId Идентификатор роли игрока
     * @param action Действие
     * @return Переадресация
     */
    @GetMapping("/player/{id}/roles/{pr}/{action}")
    public String teamRoleAction(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @PathVariable(value = "pr") Long prId, @PathVariable(value = "action") String action) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) == AppService.Role.OWNER) {
                PlayerRole playerRole = service.getPlayerRoleById(prId);
                switch (action) {
                    case "accept":
                        playerRole.setRole(playerRole.getRequest());
                        playerRole.setRequest(AppService.Role.NONE);
                        break;
                    case "reject":
                        playerRole.setRequest(AppService.Role.NONE);
                        break;
                    case "delete":
                        playerRole.setRole(AppService.Role.VIEWER);
                        break;
                }
                service.savePlayerRole(playerRole);
            }
        }
        return "redirect:/player/" + id;
    }

    /**
     * Метод для отображения страницы добавления игрока в команду
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/player/{id}/teams/add")
    public String playerTeamsAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/players";
        Player player = service.getPlayerById(id);
        if (player.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("player", player);
            model.addAttribute("teams", service.getUserTeamsPlayerNotIn(user, player));
            app.updateModel(user, model, page, "Игрок " + player.getName() + " - добавление в команду");
            return "player/add-team";
        }
        return "redirect:/player/" + id;
    }

    /**
     * Метод для удаления игрока
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param admin Флаг администратора
     * @return Переадресация
     */
    @GetMapping("/player/{id}/delete/{admin}")
    public String playerDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @PathVariable(value = "admin", required = false) Boolean admin) {
        if (user == null) return "redirect:/players";
        Player player = service.getPlayerById(id);
        if ((admin && user.isAdmin()) || (!admin && player.getUserRole(user) == AppService.Role.OWNER)) service.deletePlayer(player);
        if (admin) return "redirect:/admin?subpage=players";
        else return "redirect:/players";
    }

    /**
     * Метод для создания нового игрока
     * @param user Пользователь
     * @param name Имя
     * @param privat Приватность
     * @return Переадресация
     */
    @PostMapping("/post/player/add")
    public String postPlayerAdd(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Player newPlayer = new Player(name, privat, user);
            service.addNewPlayer(newPlayer);
            service.addNewPlayerRole(new PlayerRole(newPlayer, user, AppService.Role.OWNER));
        }
        return "redirect:/players?subpage=" + AppService.Subpage.USER_OWNER;
    }

    /**
     * Метод для добавления игрока в команду
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param team Команда
     * @param position Позиция
     * @return Переадресация
     */
    @PostMapping("/post/player/{id}/teams/add")
    public String postPlayerTeamAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Team team, @RequestParam String position) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player.getUserRole(user) >= AppService.Role.MANAGER && team.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewTeamPlayer(new TeamPlayer(team, player, position));
            }
        }
        return "redirect:/player/" + id;
    }

    /**
     * Метод для редактирования игрока по идентификатору
     * @param user Пользователь
     * @param id Идентификатор игрока
     * @param name Имя
     * @param privat Приватность
     * @return Переадресация
     */
    @PostMapping("/post/player/{id}/edit")
    public String postPlayerEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @RequestParam String name, @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Player player = service.getPlayerById(id);
            if (player != null && player.getUserRole(user) >= AppService.Role.MANAGER) {
                player.setName(name);
                player.setPrivate(privat);
                service.savePlayer(player);
            }
        }
        return "redirect:/player/" + id;
    }

}
