package ink.champ.models;

import ink.champ.service.AppService;

import javax.persistence.*;
import java.util.Set;

@Entity(name="teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    private boolean privat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(targetEntity = TeamPlayer.class, mappedBy = "team", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<TeamPlayer> players;

    @OneToMany(targetEntity = ChampTeam.class, mappedBy = "team", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<ChampTeam> champs;

    @OneToMany(targetEntity = TeamRole.class, mappedBy = "team", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<TeamRole> roles;

    public Team() { }
    public Team(String name, boolean privat, User user) {
        this.name = name;
        this.privat = privat;
        this.user = user;
    }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean privat) { this.privat = privat; }
    public void setUser(User user) { this.user = user; }
    public void setPlayers(Set<TeamPlayer> players) { this.players = players; }
    public void setChamps(Set<ChampTeam> champs) { this.champs = champs; }
    public void setRoles(Set<TeamRole> roles) { this.roles = roles; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return privat; }
    public User getUser() { return user; }
    public Set<TeamPlayer> getPlayers() { return players; }
    public Set<ChampTeam> getChamps() { return champs; }
    public Set<TeamRole> getRoles() { return roles; }

    public int getPlayersCount() { return players.size(); }
    public int getChampsCount() { return champs.size(); }
    public TeamRole getTeamRole(User user) {
        if (user != null) {
            for (TeamRole role : roles) {
                if (role.getUser().getId().equals(user.getId())) return role;
            }
        }
        return null;
    }
    public int getUserRole(User user) {
        TeamRole role = getTeamRole(user);
        return role == null ? AppService.Role.NONE : role.getRole();
    }
}
