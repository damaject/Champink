package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности пользовательской роли чемпионата
 * @author Maxim
 */
@Entity(name="champ_roles")
public class ChampRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int role;
    private int request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "champ_id")
    private Champ champ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Конструктор роли чемпионата
     */
    public ChampRole() { }

    /**
     * Конструктор роли чемпионата
     * @param champ Чемпионат
     * @param user Пользователь
     * @param role Роль
     */
    public ChampRole(Champ champ, User user, int role) {
        this.champ = champ;
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
     * Метод для установки чемпионата
     * @param champ Чемпионат
     */
    public void setChamp(Champ champ) { this.champ = champ; }

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
     * Метод для получения чемпионата
     * @return Чемпионат
     */
    public Champ getChamp() { return champ; }

    /**
     * Метод для получения пользователя
     * @return Пользователь
     */
    public User getUser() { return user; }

}
