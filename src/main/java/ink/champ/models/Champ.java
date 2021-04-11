package ink.champ.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="champs")
public class Champ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sportId;
    private String name;

    public Long getId() { return id; }
    public Long getSportId() { return sportId; }
    public String getName() { return name; }
    public void setId(Long id) { this.id = id; }
    public void setSportId(Long sportId) { this.sportId = sportId; }
    public void setName(String name) { this.name = name; }

}
