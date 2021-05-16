package ink.champ.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс-модель для представления сущности события чемпионата
 * @author Maxim
 */
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

    private int gol1;
    private int gol2;
    private int pen1;
    private int pen2;

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
    public void setGol1(int gol1) { this.gol1 = gol1; }
    public void setGol2(int gol2) { this.gol2 = gol2; }
    public void setPen1(int pen1) { this.pen1 = pen1; }
    public void setPen2(int pen2) { this.pen2 = pen2; }

    public Long getId() { return id; }
    public Champ getChamp() { return champ; }
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public Date getTimestamp() { return timestamp; }
    public int getGol1() { return gol1; }
    public int getGol2() { return gol2; }
    public int getPen1() { return pen1; }
    public int getPen2() { return pen2; }

    public void setScore(int gol1, int gol2, int pen1, int pen2) {
        this.gol1 = gol1;
        this.gol2 = gol2;
        this.pen1 = pen1;
        this.pen2 = pen2;
    }
}
