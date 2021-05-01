package ink.champ.models;

import javax.persistence.*;

@Entity(name="player_roles")
public class PlayerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public PlayerRole() { }
    public PlayerRole(Player player, User user, Integer role) {
        this.player = player;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(Integer role) { this.role = role; }
    public void setPlayer(Player player) { this.player = player; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public Integer getRole() { return role; }
    public Player getPlayer() { return player; }
    public User getUser() { return user; }
}
