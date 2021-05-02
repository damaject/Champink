package ink.champ.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name="champ_events")
public class ChampEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "champ_id")
    private Champ champ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public ChampEvent() { }
    public ChampEvent(Champ champ, Team team1, Team team2, Date timestamp) {
        this.champ = champ;
        this.team1 = team1;
        this.team2 = team2;
        this.timestamp = timestamp;
    }

    public void setId(Long id) { this.id = id; }
    public void setChamp(Champ champ) { this.champ = champ; }
    public void setTeam1(Team team1) { this.team1 = team1; }
    public void setTeam2(Team team2) { this.team2 = team2; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public Long getId() { return id; }
    public Champ getChamp() { return champ; }
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public Date getTimestamp() { return timestamp; }
}
