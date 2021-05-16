package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности пользовательской роли команды
 * @author Maxim
 */
@Entity(name="team_roles")
public class TeamRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int role;
    private int request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Конструктор роли команды
     */
    public TeamRole() { }

    /**
     * Конструктор роли команды
     * @param team Команды
     * @param user Пользователь
     * @param role Роль
     */
    public TeamRole(Team team, User user, int role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки роли
     * @param role Роль
     */
    public void setRole(int role) { this.role = role; }

    /**
     * Метод для установки запроса на роль
     * @param request Запрос на роль
     */
    public void setRequest(int request) { this.request = request; }

    /**
     * Метод для установки команды
     * @param team Команда
     */
    public void setTeam(Team team) { this.team = team; }

    /**
     * Метод для установки пользователя
     * @param user Пользователь
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения роли
     * @return Роль
     */
    public int getRole() { return role; }

    /**
     * Метод для получения запроса на роль
     * @return Запрос на роль
     */
    public int getRequest() { return request; }

    /**
     * Метод для получения команды
     * @return Команда
     */
    public Team getTeam() { return team; }

    /**
     * Метод для получения пользователя
     * @return Пользователь
     */
    public User getUser() { return user; }

}
