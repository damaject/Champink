package ink.champ.models;

import ink.champ.service.AppService;

import javax.persistence.*;
import java.util.Set;

/**
 * Класс-модель для представления сущности игрока
 * @author Maxim
 */
@Entity(name="players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    private boolean privat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OrderBy("id ASC")
    @OneToMany(targetEntity = TeamPlayer.class, mappedBy = "player", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TeamPlayer> teams;

    @OneToMany(targetEntity = PlayerRole.class, mappedBy = "player", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PlayerRole> roles;

    /**
     * Конструктор игрока
     */
    public Player() { }

    /**
     * Конструктор игрока
     * @param name Имя
     * @param privat Приватность
     * @param user Пользователь
     */
    public Player(String name, boolean privat, User user) {
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
     * Метод для установки имени
     * @param name Имя
     */
    public void setName(String name) { this.name = name; }

    /**
     * Метод для установки приватности
     * @param privat Приватность
     */
    public void setPrivate(boolean privat) { this.privat = privat; }

    /**
     * Метод для установки пользователя
     * @param user Пользователь
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Метод для установки команд игрока
     * @param teams Команды игрока
     */
    public void setTeams(Set<TeamPlayer> teams) { this.teams = teams; }

    /**
     * Метод для установки ролей игрока
     * @param roles Роли игрока
     */
    public void setRoles(Set<PlayerRole> roles) { this.roles = roles; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения имени
     * @return Имя
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
     * Метод для получения команд игрока
     * @return Команды игрока
     */
    public Set<TeamPlayer> getTeams() { return teams; }

    /**
     * Метод для получения ролей игрока
     * @return Роли игрока
     */
    public Set<PlayerRole> getRoles() { return roles; }

    /**
     * Метод для получения количества команд игрока
     * @return Количество команд игрока
     */
    public int getTeamsCount() { return teams.size(); }

    /**
     * Метод для получения роли игрока пользователя
     * @param user Пользователь
     * @return Роль игрока
     */
    public PlayerRole getPlayerRole(User user) {
        if (user != null) {
            for (PlayerRole role : roles) {
                if (role.getUser().getId().equals(user.getId())) return role;
            }
        }
        return null;
    }

    /**
     * Метод для получения роли игрока пользователя в числовом виде
     * @param user Пользователь
     * @return Роль игрока
     */
    public int getUserRole(User user) {
        PlayerRole role = getPlayerRole(user);
        return role == null ? AppService.Role.NONE : role.getRole();
    }

    /**
     * Метод для получения запроса на роль игрока пользователя
     * @param user Пользователь
     * @return Запрос на роль игрока
     */
    public int getUserRequest(User user) {
        PlayerRole role = getPlayerRole(user);
        return role == null ? AppService.Role.NONE : role.getRequest();
    }

}
