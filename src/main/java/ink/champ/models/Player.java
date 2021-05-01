package ink.champ.models;

import ink.champ.service.AppService;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(targetEntity = TeamPlayer.class, mappedBy = "player", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<TeamPlayer> teams;

    @OneToMany(targetEntity = PlayerRole.class, mappedBy = "player", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<PlayerRole> roles;

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
    public void setTeams(Set<TeamPlayer> teams) { this.teams = teams; }
    public void setRoles(Set<PlayerRole> roles) { this.roles = roles; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return privat; }
    public User getUser() { return user; }
    public Set<TeamPlayer> getTeams() { return teams; }
    public Set<PlayerRole> getRoles() { return roles; }

    public int getTeamsCount() { return teams.size(); }
    public PlayerRole getPlayerRole(User user) {
        if (user != null) {
            for (PlayerRole role : roles) {
                if (role.getUser().getId().equals(user.getId())) return role;
            }
        }
        return null;
    }
    public int getUserRole(User user) {
        PlayerRole role = getPlayerRole(user);
        return role == null ? AppService.Role.NONE : role.getRole();
    }
}
