package ink.champ.models;

import javax.persistence.*;

@Entity(name="champ_roles")
public class ChampRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "champ_id")
    private Champ champ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ChampRole() { }
    public ChampRole(Champ champ, User user, Integer role) {
        this.champ = champ;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(Integer role) { this.role = role; }
    public void setChamp(Champ champ) { this.champ = champ; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public Integer getRole() { return role; }
    public Champ getChamp() { return champ; }
    public User getUser() { return user; }
}
