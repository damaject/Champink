package ink.champ.models;

import javax.persistence.*;

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

    public Player() { }
    public Player(String name, boolean privat, User user) {
        this.name = name;
        this.privat = privat;
        this.user = user;
    }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean privat) { this.privat = privat; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return privat; }
    public User getUser() { return user; }
}
