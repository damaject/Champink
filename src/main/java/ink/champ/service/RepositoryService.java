package ink.champ.service;

import ink.champ.models.*;
import ink.champ.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс-сервис для работы со всеми репозиториями
 * @author Maxim
 */
@Service
public class RepositoryService {

    @Autowired private SportRepository sports;
    @Autowired private ChampRepository champs;
    @Autowired private ChampTeamRepository champTeams;
    @Autowired private ChampEventRepository champEvents;
    @Autowired private ChampRoleRepository champRoles;
    @Autowired private TeamRepository teams;
    @Autowired private TeamPlayerRepository teamPlayers;
    @Autowired private TeamRoleRepository teamRoles;
    @Autowired private PlayerRepository players;
    @Autowired private PlayerRoleRepository playerRoles;
    @Autowired private UserRepository users;

    private Sort sortDescId = Sort.by(Sort.Direction.DESC, "id");

    /**
     * Метод для получения списка пользователей
     * @param search Часть имени для поиска
     * @return Список пользователей
     */
    public List<User> getUsers(String search) {
        return users.findUserByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, search, sortDescId);
    }

    /**
     * Метод для получения списка видов спорта
     * @param search Часть названия для поиска
     * @return Список видов спорта
     */
    public List<Sport> getSports(String search) {
        return sports.findSportByNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка чемпионатов
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    public List<Champ> getChamps(String search) {
        return champs.findChampsByNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка команд
     * @param search Часть названия для поиска
     * @return Список команд
     */
    public List<Team> getTeams(String search) {
        return teams.findTeamsByNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка игроков
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    public List<Player> getPlayers(String search) {
        return players.findPlayersByNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка пользовательских игроков которые не состоят в команде
     * @param user Пользователь
     * @param team Команда
     * @return Список игроков
     */
    public List<Player> getUserPlayersNotInTeam(User user, Team team) {
        return players.findPlayersByUserRoleAndNotInTeam(team, user);
    }

    /**
     * Метод для получения списка пользовательских команд которые не состоят в чемпионате
     * @param user Пользователь
     * @param champ Чемпионат
     * @return Список команд
     */
    public List<Team> getUserTeamsNotInChamp(User user, Champ champ) {
        return teams.findTeamsByUserRoleAndNotInChamp(champ, user);
    }

    /**
     * Метод для получения списка пользовательских команд в которых не состоит игрок
     * @param user Пользователь
     * @param player Игрок
     * @return Список команд
     */
    public List<Team> getUserTeamsPlayerNotIn(User user, Player player) {
        return teams.findTeamsByUserRoleAndPlayerNotIn(player, user);
    }

    /**
     * Метод для получения списка пользовательских чемпионатов в которых не участвует команда
     * @param user Пользователь
     * @param team Команда
     * @return Список чемпионатов
     */
    public List<Champ> getUserChampsTeamNotIn(User user, Team team) {
        return champs.findChampsByUserRoleAndTeamNotIn(team, user);
    }

    /**
     * Метод для получения списка публичных чемпионатов с фильтром по названию
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    public List<Champ> getGlobalChamps(String search) {
        return champs.findChampsByPrivatIsFalseAndNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка пользовательских чемпионатов с фильтром по названию
     * @param user Пользователь
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    public List<Champ> getUserChampsAll(User user, String search) {
        return champs.findChampsByUserAll(user, search);
    }

    /**
     * Метод для получения списка пользовательских чемпионатов по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    public List<Champ> getUserChampsRole(User user, int role, String search) {
        return champs.findChampsByUserRole(user, role, search);
    }

    /**
     * Метод для получения списка публичных команд с фильтром по названию
     * @param search Часть названия для поиска
     * @return Список команд
     */
    public List<Team> getGlobalTeams(String search) {
        return teams.findTeamsByPrivatIsFalseAndNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка пользовательских команд с фильтром по названию
     * @param user Пользователь
     * @param search Часть названия для поиска
     * @return Список команд
     */
    public List<Team> getUserTeamsAll(User user, String search) {
        return teams.findTeamsByUserAll(user, search);
    }

    /**
     * Метод для получения списка пользовательских команд по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть названия для поиска
     * @return Список команд
     */
    public List<Team> getUserTeamsRole(User user, int role, String search) {
        return teams.findTeamsByUserRole(user, role, search);
    }

    /**
     * Метод для получения списка публичных игроков с фильтром по имени
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    public List<Player> getGlobalPlayers(String search) {
        return players.findPlayersByPrivatIsFalseAndNameContainingIgnoreCase(search, sortDescId);
    }

    /**
     * Метод для получения списка пользовательских игроков с фильтром по названию
     * @param user Пользователь
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    public List<Player> getUserPlayersAll(User user, String search) {
        return players.findPlayersByUserAll(user, search);
    }

    /**
     * Метод для получения списка пользовательских игроков по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    public List<Player> getUserPlayersRole(User user, int role, String search) {
        return players.findPlayersByUserRole(user, role, search);
    }

    /**
     * Метод для получения пользователя по идентификатору
     * @param id Идентификатор пользователя
     * @return Пользователь
     */
    public User getUserById(Long id) { return users.findById(id).orElse(null); }

    /**
     * Метод для получения вида спорта по идентификатору
     * @param id Идентификатор вида спорта
     * @return Вид спорта
     */
    public Sport getSportById(Long id) { return sports.findById(id).orElse(null); }

    /**
     * Метод для получения чемпионата по идентификатору
     * @param id Идентификатор чемпионата
     * @return Чемпионат
     */
    public Champ getChampById(Long id) { return champs.findById(id).orElse(null); }

    /**
     * Метод для получения роли чемпионата по идентификатору
     * @param id Идентификатор роли чемпионата
     * @return Роль чемпионата
     */
    public ChampRole getChampRoleById(Long id) { return champRoles.findById(id).orElse(null); }

    /**
     * Метод для получения команды чемпионата по идентификатору
     * @param id Идентификатор команды чемпионата
     * @return Команда чемпионата
     */
    public ChampTeam getChampTeamById(Long id) { return champTeams.findById(id).orElse(null); }

    /**
     * Метод для получения события чемпионата по идентификатору
     * @param id Идентификатор события чемпионата
     * @return Событие чемпионата
     */
    public ChampEvent getChampEventById(Long id) { return champEvents.findById(id).orElse(null); }

    /**
     * Метод для получения команды по идентификатору
     * @param id Идентификатор команды
     * @return Команда
     */
    public Team getTeamById(Long id) { return teams.findById(id).orElse(null); }

    /**
     * Метод для получения роли команды по идентификатору
     * @param id Идентификатор роли команды
     * @return Роль команды
     */
    public TeamRole getTeamRoleById(Long id) { return teamRoles.findById(id).orElse(null); }

    /**
     * Метод для получения игрока команды по идентификатору
     * @param id Идентификатор игрока команды
     * @return Игрок команды
     */
    public TeamPlayer getTeamPlayerById(Long id) { return teamPlayers.findById(id).orElse(null); }

    /**
     * Метод для получения игрока по идентификатору
     * @param id Идентификатор игрока
     * @return Игрок
     */
    public Player getPlayerById(Long id) { return players.findById(id).orElse(null); }

    /**
     * Метод для получения роли игрока по идентификатору
     * @param id Идентификатор роли игрока
     * @return Роль игрока
     */
    public PlayerRole getPlayerRoleById(Long id) { return playerRoles.findById(id).orElse(null); }

    /**
     * Метод для получения пользователя по имени
     * @param username Имя пользователя
     * @return Пользователь
     */
    public User getUserByUsername(String username) { return users.findByUsername(username); }

    /**
     * Метод для добавления нового пользователя
     * @param user Пользователь
     */
    public void addNewUser(User user) { saveUser(user); }

    /**
     * Метод для добавления нового чемпионата
     * @param champ Чемпионат
     */
    public void addNewChamp(Champ champ) { champs.save(champ); }

    /**
     * Метод для добавления новой команды чемпионата
     * @param champTeam Команда чемпионата
     */
    public void addNewChampTeam(ChampTeam champTeam) { champTeams.save(champTeam); }

    /**
     * Метод для добавления новой роли чемпионата
     * @param role Роль чемпионата
     */
    public void addNewChampRole(ChampRole role) { saveChampRole(role);}

    /**
     * Метод для добавления нового события чемпионата
     * @param event Событие чемпионата
     */
    public void addNewChampEvent(ChampEvent event) { champEvents.save(event); }

    /**
     * Метод для добавления новой команды
     * @param team Команда
     */
    public void addNewTeam(Team team) { teams.save(team); }

    /**
     * Метод для добавления нового игрока команды
     * @param teamPlayer Игрок команды
     */
    public void addNewTeamPlayer(TeamPlayer teamPlayer) { teamPlayers.save(teamPlayer); }

    /**
     * Метод для добавления новой роли команды
     * @param role Роль команды
     */
    public void addNewTeamRole(TeamRole role) { saveTeamRole(role); }

    /**
     * Метод для добавления нового игрока
     * @param player Игрок
     */
    public void addNewPlayer(Player player) { players.save(player); }

    /**
     * Метод для добавления новой роли игрока
     * @param role Роль игрока
     */
    public void addNewPlayerRole(PlayerRole role) { savePlayerRole(role); }

    /**
     * Метод для сохранения пользователя
     * @param user Пользователь
     */
    public void saveUser(User user) { users.save(user); }

    /**
     * Метод для сохранения чемпионата
     * @param champ Чемпионат
     */
    public void saveChamp(Champ champ) { champs.save(champ); }

    /**
     * Метод для сохранения команды
     * @param team Команда
     */
    public void saveTeam(Team team) { teams.save(team); }

    /**
     * Метод для сохранения игрока
     * @param player Игрок
     */
    public void savePlayer(Player player) { players.save(player); }

    /**
     * Метод для сохранения роли чемпионата
     * @param role Роль чемпионата
     */
    public void saveChampRole(ChampRole role) { champRoles.save(role); }

    /**
     * Метод для сохранения роли команды
     * @param role Роль команды
     */
    public void saveTeamRole(TeamRole role) { teamRoles.save(role); }

    /**
     * Метод для сохранения роли игрока
     * @param role Роль игрока
     */
    public void savePlayerRole(PlayerRole role) { playerRoles.save(role); }

    /**
     * Метод для сохранения события чемпионата
     * @param champEvent Событие чемпионата
     */
    public void saveChampEvent(ChampEvent champEvent) { champEvents.save(champEvent); }

    /**
     * Метод для удаления пользователя
     * @param user Пользователь
     */
    public void deleteUser(User user) { users.delete(user); }

    /**
     * Метод для удаления чемпионата
     * @param champ Чемпионат
     */
    public void deleteChamp(Champ champ) { champs.delete(champ); }

    /**
     * Метод для удаления команды
     * @param team Команда
     */
    public void deleteTeam(Team team) { teams.delete(team); }

    /**
     * Метод для удаления игрока
     * @param player Игрок
     */
    public void deletePlayer(Player player) { players.delete(player); }

    /**
     * Метод для удаления игрока команды
     * @param teamPlayer Игрок команды
     */
    public void deleteTeamPlayer(TeamPlayer teamPlayer) { teamPlayers.delete(teamPlayer); }

    /**
     * Метод для удаления команды чемпионата
     * @param champTeam Команда чемпионата
     */
    public void deleteChampTeam(ChampTeam champTeam) { champTeams.delete(champTeam); }

    /**
     * Метод для удаления события чемпионата
     * @param champEvent Событие чемпионата
     */
    public void deleteChampEvent(ChampEvent champEvent) { champEvents.delete(champEvent); }

}
