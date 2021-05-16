package ink.champ.models;

import ink.champ.service.AppService;

import javax.persistence.*;
import java.util.Set;

/**
 * Класс-модель для представления сущности чемпионата
 * @author Maxim
 */
@Entity(name="champs")
public class Champ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    @Column(length = 25)
    private String format;

    private boolean privat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OrderBy("id DESC")
    @OneToMany(targetEntity = ChampTeam.class, mappedBy = "champ", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChampTeam> teams;

    @OneToMany(targetEntity = ChampRole.class, mappedBy = "champ", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChampRole> roles;

    @OrderBy("timestamp ASC")
    @OneToMany(targetEntity = ChampEvent.class, mappedBy = "champ", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChampEvent> events;

    /**
     * Конструктор чемпионата
     */
    public Champ() { }

    /**
     * Конструктор чемпионата
     * @param name Название
     * @param format Формат
     * @param privat Приватность
     * @param sport Вид спорта
     * @param user Пользователь
     */
    public Champ(String name, String format, boolean privat, Sport sport, User user) {
        this.name = name;
        this.format = format;
        this.privat = privat;
        this.sport = sport;
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
     * Метод для установки формата
     * @param format Формат
     */
    public void setFormat(String format) { this.format = format; }

    /**
     * Метод для установки приватности
     * @param privat Приватность
     */
    public void setPrivate(boolean privat) { this.privat = privat; }

    /**
     * Метод для установки вида спорта
     * @param sport Вид спорта
     */
    public void setSport(Sport sport) { this.sport = sport; }

    /**
     * Метод для установки пользователя
     * @param user Пользователя
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Метод для установки команд чемпионата
     * @param teams Команды чемпионата
     */
    public void setTeams(Set<ChampTeam> teams) { this.teams = teams; }

    /**
     * Метод для установки ролей чемпионата
     * @param roles Роли чемпионата
     */
    public void setRoles(Set<ChampRole> roles) { this.roles = roles; }

    /**
     * Метод для установки событий чемпионата
     * @param events События чемпионата
     */
    public void setEvents(Set<ChampEvent> events) { this.events = events; }

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
     * Метод для получения формата
     * @return Формат
     */
    public String getFormat() { return format; }

    /**
     * Метод для получения приватности
     * @return Приватность
     */
    public boolean isPrivate() { return privat; }

    /**
     * Метод для получения вида спорта
     * @return Вид спорта
     */
    public Sport getSport() { return sport; }

    /**
     * Метод для получения пользователя
     * @return Пользователь
     */
    public User getUser() { return user; }

    /**
     * Метод для получения команд чемпионата
     * @return Команды чемпионата
     */
    public Set<ChampTeam> getTeams() { return teams; }

    /**
     * Метод для получения ролей чемпионата
     * @return Роли чемпионата
     */
    public Set<ChampRole> getRoles() { return roles; }

    /**
     * Метод для получения событий чемпионата
     * @return События чемпионата
     */
    public Set<ChampEvent> getEvents() { return events; }

    /**
     * Метод для получения количества команд чемпионата
     * @return Количество команд чемпионата
     */
    public int getTeamsCount() { return teams.size(); }

    /**
     * Метод для получения количества событий чемпионата
     * @return Количество событий чемпионата
     */
    public int getEventsCount() { return events.size(); }

    /**
     * Метод для получения роли чемпионата пользователя
     * @param user Пользователь
     * @return Роль чемпионата
     */
    public ChampRole getChampRole(User user) {
        if (user != null) {
            for (ChampRole role : roles) {
                if (role.getUser().getId().equals(user.getId())) return role;
            }
        }
        return null;
    }

    /**
     * Метод для получения роли чемпионата пользователя в числовом виде
     * @param user Пользователь
     * @return Роль чемпионата
     */
    public int getUserRole(User user) {
        ChampRole role = getChampRole(user);
        return role == null ? AppService.Role.NONE : role.getRole();
    }

    /**
     * Метод для получения запроса на роль чемпионата пользователя
     * @param user Пользователь
     * @return Запрос на роль чемпионата
     */
    public int getUserRequest(User user) {
        ChampRole role = getChampRole(user);
        return role == null ? AppService.Role.NONE : role.getRequest();
    }

}
