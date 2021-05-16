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

    /**
     * Конструктор события чемпионата
     */
    public ChampEvent() { }

    /**
     * Конструктор события чемпионата
     * @param champ Чемпионат
     * @param team1 Команда 1
     * @param team2 Команда 2
     * @param timestamp Дата и время
     */
    public ChampEvent(Champ champ, Team team1, Team team2, Date timestamp) {
        this.champ = champ;
        this.team1 = team1;
        this.team2 = team2;
        this.timestamp = timestamp;
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
     * Метод для установки команды 1
     * @param team1 Команда 1
     */
    public void setTeam1(Team team1) { this.team1 = team1; }

    /**
     * Метод для установки команды 2
     * @param team2 Команда 2
     */
    public void setTeam2(Team team2) { this.team2 = team2; }

    /**
     * Метод для установки даты и время
     * @param timestamp Дата и время
     */
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    /**
     * Метод для установки голов команды 1
     * @param gol1 Голы команды 1
     */
    public void setGol1(int gol1) { this.gol1 = gol1; }

    /**
     * Метод для установки голов команды 2
     * @param gol2 Голы команды 2
     */
    public void setGol2(int gol2) { this.gol2 = gol2; }

    /**
     * Метод для установки пенальти команды 1
     * @param pen1 Пенальти команды 1
     */
    public void setPen1(int pen1) { this.pen1 = pen1; }

    /**
     * Метод для установки пенальти команды 2
     * @param pen2 Пенальти команды 2
     */
    public void setPen2(int pen2) { this.pen2 = pen2; }

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
     * Метод для получения команды 1
     * @return Команда 1
     */
    public Team getTeam1() { return team1; }

    /**
     * Метод для получения команды 2
     * @return Команда 2
     */
    public Team getTeam2() { return team2; }

    /**
     * Метод для получения даты и время
     * @return Дата и время
     */
    public Date getTimestamp() { return timestamp; }

    /**
     * Метод для получения голов команды 1
     * @return Голы команды 1
     */
    public int getGol1() { return gol1; }

    /**
     * Метод для получения голов команды 2
     * @return Голы команды 2
     */
    public int getGol2() { return gol2; }

    /**
     * Метод для получения пенальти команды 1
     * @return Пенальти команды 1
     */
    public int getPen1() { return pen1; }

    /**
     * Метод для получения пенальти команды 2
     * @return Пенальти команды 2
     */
    public int getPen2() { return pen2; }

    /**
     * Метод для установки голов и пенальти команд
     * @param gol1 Голы команды 1
     * @param gol2 Голы команды 2
     * @param pen1 Пенальти команды 1
     * @param pen2 Пенальти команды 2
     */
    public void setScore(int gol1, int gol2, int pen1, int pen2) {
        this.gol1 = gol1;
        this.gol2 = gol2;
        this.pen1 = pen1;
        this.pen2 = pen2;
    }

}
