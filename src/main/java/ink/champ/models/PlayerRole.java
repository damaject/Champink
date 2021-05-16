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

    public PlayerRole() { }
    public PlayerRole(Player player, User user, int role) {
        this.player = player;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(int role) { this.role = role; }
    public void setRequest(int request) { this.request = request; }
    public void setPlayer(Player player) { this.player = player; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public int getRole() { return role; }
    public int getRequest() { return request; }
    public Player getPlayer() { return player; }
    public User getUser() { return user; }
}
