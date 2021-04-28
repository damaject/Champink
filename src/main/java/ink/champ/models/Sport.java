package ink.champ.models;

import javax.persistence.*;

@Entity(name="sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    public Sport() { }
    public Sport(String name) {
        this.name = name;
    }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}
