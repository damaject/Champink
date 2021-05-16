package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности команды чемпионата
 * @author Maxim
 */
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

    /**
     * Конструктор команды чемпионата
     */
    public ChampTeam() { }

    /**
     * Конструктор команды чемпионата
     * @param champ Чемпионат
     * @param team Команда
     */
    public ChampTeam(Champ champ, Team team) {
        this.champ = champ;
        this.team = team;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки чемпионата
     * @param champ Чемпионат
     */
    public void setChamp(Champ champ) { this.champ = champ; }

    /**
     * Метод для установки команды
     * @param team Команда
     */
    public void setTeam(Team team) { this.team = team; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения чемпионата
     * @return Чемпионат
     */
    public Champ getChamp() { return champ; }

    /**
     * Метод для получения команды
     * @return Команда
     */
    public Team getTeam() { return team; }

}
