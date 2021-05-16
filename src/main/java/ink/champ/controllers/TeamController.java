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
 * Класс-контроллер для обработки запросов раздела команд
 * @author Maxim
 */
@Controller
public class TeamController {

    @Autowired private AppService app;
    @Autowired private RepositoryService service;

    private final String page = "team";

    /**
     * Метод для отображения страницы команд
     * @param subpage Раздел страницы
     * @param search Часть названия для поиска
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/teams")
    public String teams(@RequestParam(required = false) String subpage, @RequestParam(required = false) String search,
                        @AuthenticationPrincipal User user, Model model) {
        if (subpage != null) {
            app.subpage = subpage;
            return "redirect:/teams";
        }
        if (app.subpage == null) app.subpage = AppService.Subpage.GLOBAL;

        String s = "";
        String title = "Команды";
        if (search != null && !search.isEmpty()) {
            s = search;
            title += " - поиск «" + search + "»";
        }

        switch (app.subpage) {
            case AppService.Subpage.GLOBAL:
                model.addAttribute("teams", service.getGlobalTeams(s));
                break;
            case AppService.Subpage.USER_ALL:
                model.addAttribute("teams", service.getUserTeamsAll(user, s));
                break;
            case AppService.Subpage.USER_OWNER:
                model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.OWNER, s));
                break;
            case AppService.Subpage.USER_MANAGER:
                model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.MANAGER, s));
                break;
            case AppService.Subpage.USER_VIEWER:
                model.addAttribute("teams", service.getUserTeamsRole(user, AppService.Role.VIEWER, s));
                break;
            default:
                return "redirect:/teams?subpage=" + AppService.Subpage.GLOBAL;
        }

        model.addAttribute("headerTitle", title);
        app.updateModel(user, model, page, title);
        return "team/list";
    }

    /**
     * Метод для отображения страницы команды по идентификатору
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/team/{id}")
    public String teamById(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        Team team = service.getTeamById(id);
        if (team == null) return "redirect:/teams";
        model.addAttribute("team", team);
        app.updateModel(user, model, page, "Команда " + team.getName());
        return "team/view";
    }

    /**
     * Метод для отображения страницы создания новой команды
     * @param user Пользователь
     * @param model Модель
     * @return Шаблон
     */
    @GetMapping("/team/new")
    public String teamNew(@AuthenticationPrincipal User user, Model model) {
        app.subpage = AppService.Subpage.USER_OWNER;
        app.updateModel(user, model, page, "Новая команда");
        return "team/new";
    }

    /**
     * Метод для отображения страницы редактирования команды по идентификатору
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/team/{id}/edit")
    public String teamEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) >= AppService.Role.MANAGER) {
                model.addAttribute("team", team);
                app.updateModel(user, model, page, "Команда " + team.getName() + " - редактирование");
                return "team/edit";
            }
        }
        return "redirect:/teams";
    }

    /**
     * Метод для установки роли команды
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param role Роль
     * @return Переадресация
     */
    @GetMapping("/team/{id}/roles/set")
    public String teamRoleSet(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, @RequestParam Integer role) {
        if (user != null) {
            Team team = service.getTeamById(id);
            TeamRole teamRole = team.getTeamRole(user);
            if (teamRole == null) {
                teamRole = new TeamRole(team, user, AppService.Role.NONE);
                service.addNewTeamRole(teamRole);
            }
            if ((role == 0 || role == 1) && teamRole.getRole() < AppService.Role.OWNER) {
                teamRole.setRole(role);
                teamRole.setRequest(AppService.Role.NONE);
                service.saveTeamRole(teamRole);
            }
            else if (role == AppService.Role.MANAGER && teamRole.getRole() < AppService.Role.MANAGER) {
                teamRole.setRequest(role);
                service.saveTeamRole(teamRole);
            }
        }

        return "redirect:/team/" + id;
    }

    /**
     * Метод для принятия, отклонения или удаления роли команды
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param trId Идентификатор роли команды
     * @param action Действие
     * @return Переадресация
     */
    @GetMapping("/team/{id}/roles/{tr}/{action}")
    public String teamRoleAction(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @PathVariable(value = "tr") Long trId, @PathVariable(value = "action") String action) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) == AppService.Role.OWNER) {
                TeamRole teamRole = service.getTeamRoleById(trId);
                switch (action) {
                    case "accept":
                        teamRole.setRole(teamRole.getRequest());
                        teamRole.setRequest(AppService.Role.NONE);
                        break;
                    case "reject":
                        teamRole.setRequest(AppService.Role.NONE);
                        break;
                    case "delete":
                        teamRole.setRole(AppService.Role.VIEWER);
                        break;
                }
                service.saveTeamRole(teamRole);
            }
        }
        return "redirect:/team/" + id;
    }

    /**
     * Метод для отображения страницы добавления игрока в команду
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/team/{id}/players/add")
    public String teamPlayersAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if (team.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("team", team);
            model.addAttribute("players", service.getUserPlayersNotInTeam(user, team));
            app.updateModel(user, model, page, "Команда " + team.getName() + " - добавление игрока");
            return "team/add-player";
        }
        return "redirect:/team/" + id;
    }

    /**
     * Метод для отображения страницы добавления команды в чемпионат
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param model Модель
     * @return Шаблон или переадресация
     */
    @GetMapping("/team/{id}/champs/add")
    public String teamChampsAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id, Model model) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if (team.getUserRole(user) >= AppService.Role.MANAGER) {
            model.addAttribute("team", team);
            model.addAttribute("champs", service.getUserChampsTeamNotIn(user, team));
            app.updateModel(user, model, page, "Команда " + team.getName() + " - добавление в чемпионат");
            return "team/add-champ";
        }
        return "redirect:/team/" + id;
    }

    /**
     * Метод для удаления игрока из команды
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param tpId Идентификатор игрока команды
     * @return Переадресация
     */
    @GetMapping("/team/{id}/players/{tp}/delete")
    public String teamPlayersDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                             @PathVariable(value = "tp") Long tpId) {
        if (user == null) return "redirect:/teams";
        TeamPlayer teamPlayer = service.getTeamPlayerById(tpId);
        if (teamPlayer.getTeam().getUserRole(user) >= AppService.Role.MANAGER) service.deleteTeamPlayer(teamPlayer);
        return "redirect:/team/" + id;
    }

    /**
     * Метод для удаления команды
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param admin Флаг администратора
     * @return Переадресация
     */
    @GetMapping("/team/{id}/delete/{admin}")
    public String teamDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                               @PathVariable(value = "admin", required = false) Boolean admin) {
        if (user == null) return "redirect:/teams";
        Team team = service.getTeamById(id);
        if ((admin && user.isAdmin()) || (!admin && team.getUserRole(user) == AppService.Role.OWNER)) service.deleteTeam(team);
        if (admin) return "redirect:/admin?subpage=teams";
        else return "redirect:/teams";
    }

    /**
     * Метод для создания новой команды
     * @param user Пользователь
     * @param name Название
     * @param privat Приватность
     * @return Переадресация
     */
    @PostMapping("/post/team/add")
    public String postTeamAdd(@AuthenticationPrincipal User user, @RequestParam String name,
                               @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Team newTeam = new Team(name, privat, user);
            service.addNewTeam(newTeam);
            service.addNewTeamRole(new TeamRole(newTeam, user, AppService.Role.OWNER));
        }
        return "redirect:/teams?subpage=" + AppService.Subpage.USER_OWNER;
    }

    /**
     * Метод для добавления игрока в команду
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param player Игрок
     * @param position Позиция
     * @return Переадресация
     */
    @PostMapping("/post/team/{id}/players/add")
    public String postTeamPlayerAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Player player, @RequestParam String position) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team.getUserRole(user) >= AppService.Role.MANAGER && player.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewTeamPlayer(new TeamPlayer(team, player, position));
            }
        }
        return "redirect:/team/" + id;
    }

    /**
     * Метод для добавления команды в чемпионат
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param champ Чемпионат
     * @return Переадресация
     */
    @PostMapping("/post/team/{id}/champs/add")
    public String postTeamChampAdd(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                    @RequestParam Champ champ) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team.getUserRole(user) >= AppService.Role.MANAGER && champ.getUserRole(user) >= AppService.Role.MANAGER) {
                service.addNewChampTeam(new ChampTeam(champ, team));
            }
        }
        return "redirect:/team/" + id;
    }

    /**
     * Метод для редактирования команды по идентификатору
     * @param user Пользователь
     * @param id Идентификатор команды
     * @param name Название
     * @param privat Приватность
     * @return Переадресация
     */
    @PostMapping("/post/team/{id}/edit")
    public String postTeamEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") Long id,
                                 @RequestParam String name, @RequestParam(defaultValue = "false") boolean privat) {
        if (user != null) {
            Team team = service.getTeamById(id);
            if (team != null && team.getUserRole(user) >= AppService.Role.MANAGER) {
                team.setName(name);
                team.setPrivate(privat);
                service.saveTeam(team);
            }
        }
        return "redirect:/team/" + id;
    }

}
