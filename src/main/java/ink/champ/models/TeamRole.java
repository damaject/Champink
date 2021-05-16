package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности пользовательской роли команды
 * @author Maxim
 */
@Entity(name="team_roles")
public class TeamRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int role;
    private int request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public TeamRole() { }
    public TeamRole(Team team, User user, int role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }

    public void setId(Long id) { this.id = id; }
    public void setRole(int role) { this.role = role; }
    public void setRequest(int request) { this.request = request; }
    public void setTeam(Team team) { this.team = team; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public int getRole() { return role; }
    public int getRequest() { return request; }
    public Team getTeam() { return team; }
    public User getUser() { return user; }
}
