package ink.champ.models;

import javax.persistence.*;

@Entity(name="champs")
public class Champ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    private boolean privat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Champ() { }
    public Champ(String name, boolean privat, Sport sport, User user) {
        this.name = name;
        this.privat = privat;
        this.sport = sport;
        this.user = user;
    }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean privat) { this.privat = privat; }
    public void setSport(Sport sport) { this.sport = sport; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return privat; }
    public Sport getSport() { return sport; }
    public User getUser() { return user; }
}
