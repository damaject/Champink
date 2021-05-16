package ink.champ.models;

import ink.champ.service.AppService;

import javax.persistence.*;
import java.util.Set;

/**
 * Класс-модель для представления сущности команды
 * @author Maxim
 */
@Entity(name="teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    private boolean privat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OrderBy("position ASC")
    @OneToMany(targetEntity = TeamPlayer.class, mappedBy = "team", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TeamPlayer> players;

    @OneToMany(targetEntity = ChampTeam.class, mappedBy = "team", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private Set<ChampTeam> champs;

    @OneToMany(targetEntity = ChampEvent.class, mappedBy = "team1", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChampEvent> champEventsH;

    @OneToMany(targetEntity = ChampEvent.class, mappedBy = "team2", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChampEvent> champEventsA;

    @OneToMany(targetEntity = TeamRole.class, mappedBy = "team", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TeamRole> roles;

    /**
     * Конструктор команды
     */
    public Team() { }

    /**
     * Конструктор команды
     * @param name Название
     * @param privat Приватность
     * @param user Пользователь
     */
    public Team(String name, boolean privat, User user) {
        this.name = name;
        this.privat = privat;
        this.user = user;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки названия
     * @param name Название
     */
    public void setName(String name) { this.name = name; }

    /**
     * Метод для установки приватности
     * @param privat Приватность
     */
    public void setPrivate(boolean privat) { this.privat = privat; }

    /**
     * Метод для установки пользователя
     * @param user Пользователя
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Метод для установки игроков команды
     * @param players Игроки команды
     */
    public void setPlayers(Set<TeamPlayer> players) { this.players = players; }

    /**
     * Метод для установки чемпионатов команды
     * @param champs Чемпионаты команды
     */
    public void setChamps(Set<ChampTeam> champs) { this.champs = champs; }

    /**
     * Метод для установки событий команды в которых она является хозяином
     * @param champEvents События команды
     */
    public void setChampEventsH(Set<ChampEvent> champEvents) { this.champEventsH = champEvents; }

    /**
     * Метод для установки событий команды в которых она является гостем
     * @param champEvents События команды
     */
    public void setChampEventsA(Set<ChampEvent> champEvents) { this.champEventsA = champEvents; }

    /**
     * Метод для установки ролей команды
     * @param roles Роли команды
     */
    public void setRoles(Set<TeamRole> roles) { this.roles = roles; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения названия
     * @return Название
     */
    public String getName() { return name; }

    /**
     * Метод для получения приватности
     * @return Приватность
     */
    public boolean isPrivate() { return privat; }

    /**
     * Метод для получения пользователя
     * @return Пользователь
     */
    public User getUser() { return user; }

    /**
     * Метод для получения игроков команды
     * @return Игроков команды
     */
    public Set<TeamPlayer> getPlayers() { return players; }

    /**
     * Метод для получения чемпионатов команды
     * @return Чемпионатов команды
     */
    public Set<ChampTeam> getChamps() { return champs; }

    /**
     * Метод для получения событий команды в которых она является хозяином
     * @return События команды
     */
    public Set<ChampEvent> getChampEventsH() { return champEventsH; }

    /**
     * Метод для получения событий команды в которых она является гостем
     * @return События команды
     */
    public Set<ChampEvent> getChampEventsA() { return champEventsA; }

    /**
     * Метод для получения ролей команды
     * @return Роли команды
     */
    public Set<TeamRole> getRoles() { return roles; }

    /**
     * Метод для получения количества игроков команды
     * @return Количество игроков команды
     */
    public int getPlayersCount() { return players.size(); }

    /**
     * Метод для получения количества чемпионатов команды
     * @return Количество чемпионатов команды
     */
    public int getChampsCount() { return champs.size(); }

    /**
     * Метод для получения роли команды пользователя
     * @param user Пользователь
     * @return Роль команды
     */
    public TeamRole getTeamRole(User user) {
        if (user != null) {
            for (TeamRole role : roles) {
                if (role.getUser().getId().equals(user.getId())) return role;
            }
        }
        return null;
    }

    /**
     * Метод для получения роли команды пользователя в числовом виде
     * @param user Пользователь
     * @return Роль команды
     */
    public int getUserRole(User user) {
        TeamRole role = getTeamRole(user);
        return role == null ? AppService.Role.NONE : role.getRole();
    }

    /**
     * Метод для получения запроса на роль команды пользователя
     * @param user Пользователь
     * @return Запрос на роль команды
     */
    public int getUserRequest(User user) {
        TeamRole role = getTeamRole(user);
        return role == null ? AppService.Role.NONE : role.getRequest();
    }

}
