package ink.champ.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long champId;
    private Long teamId1;
    private Long teamId2;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    public Long getId() { return id; }
    public Long getChampId() { return champId; }
    public Long getTeamId1() { return teamId1; }
    public Long getTeamId2() { return teamId2; }
    public Date getStartTime() { return startTime; }
    public void setId(Long id) { this.id = id; }
    public void setChampId(Long champId) { this.champId = champId; }
    public void setTeamId1(Long teamId1) { this.teamId1 = teamId1; }
    public void setTeamId2(Long teamId2) { this.teamId2 = teamId2; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
}
