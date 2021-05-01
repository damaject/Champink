package ink.champ.models;

import javax.persistence.*;

@Entity(name="team_roles")
public class TeamRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public TeamRole() { }
    public TeamRole(Team team, User user, Integer role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(Integer role) { this.role = role; }
    public void setTeam(Team team) { this.team = team; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public Integer getRole() { return role; }
    public Team getTeam() { return team; }
    public User getUser() { return user; }
}
