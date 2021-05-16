package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности игрока команды
 * @author Maxim
 */
@Entity(name="team_players")
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(length = 15)
    private String position;

    public TeamPlayer() { }
    public TeamPlayer(Team team, Player player, String position) {
        this.team = team;
        this.player = player;
        this.position = position;
    }

    public void setId(Long id) { this.id = id; }
    public void setTeam(Team team) { this.team = team; }
    public void setPlayer(Player player) { this.player = player; }
    public void setPosition(String position) { this.position = position; }

    public Long getId() { return id; }
    public Team getTeam() { return team; }
    public Player getPlayer() { return player; }
    public String getPosition() { return position; }
}
