package ink.champ.models;

import javax.persistence.*;

@Entity(name="champ_teams")
public class ChampTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "champ_id")
    private Champ champ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    public ChampTeam() { }
    public ChampTeam(Champ champ, Team team) {
        this.champ = champ;
        this.team = team;
    }

    public void setId(Long id) { this.id = id; }
    public void setChamp(Champ champ) { this.champ = champ; }
    public void setTeam(Team team) { this.team = team; }

    public Long getId() { return id; }
    public Champ getChamp() { return champ; }
    public Team getTeam() { return team; }
}
