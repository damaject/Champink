package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности пользовательской роли игрока
 * @author Maxim
 */
@Entity(name="player_roles")
public class PlayerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int role;
    private int request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Конструктор роли игрока
     */
    public PlayerRole() { }

    /**
     * Конструктор роли игрока
     * @param player Игрока
     * @param user Пользователь
     * @param role Роль
     */
    public PlayerRole(Player player, User user, int role) {
        this.player = player;
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
     * Метод для установки игрока
     * @param player Игрок
     */
    public void setPlayer(Player player) { this.player = player; }

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
     * Метод для получения игрока
     * @return Игрок
     */
    public Player getPlayer() { return player; }

    /**
     * Метод для получения пользователя
     * @return Пользователь
     */
    public User getUser() { return user; }

}
