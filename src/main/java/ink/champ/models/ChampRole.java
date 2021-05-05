package ink.champ.models;

import javax.persistence.*;

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

    public ChampRole() { }
    public ChampRole(Champ champ, User user, int role) {
        this.champ = champ;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(int role) { this.role = role; }
    public void setRequest(int request) { this.request = request; }
    public void setChamp(Champ champ) { this.champ = champ; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public int getRole() { return role; }
    public int getRequest() { return request; }
    public Champ getChamp() { return champ; }
    public User getUser() { return user; }
}
