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

    /**
     * Конструктор игрока команды
     */
    public TeamPlayer() { }

    /**
     * Конструктор игрока команды
     * @param team Команда
     * @param player Игрок
     * @param position Позиция
     */
    public TeamPlayer(Team team, Player player, String position) {
        this.team = team;
        this.player = player;
        this.position = position;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки команды
     * @param team Команда
     */
    public void setTeam(Team team) { this.team = team; }

    /**
     * Метод для установки игрока
     * @param player Игрок
     */
    public void setPlayer(Player player) { this.player = player; }

    /**
     * Метод для установки позиции
     * @param position Позиция
     */
    public void setPosition(String position) { this.position = position; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения команды
     * @return Команда
     */
    public Team getTeam() { return team; }

    /**
     * Метод для получения игрока
     * @return Игрок
     */
    public Player getPlayer() { return player; }

    /**
     * Метод для получения позиции
     * @return Позиция
     */
    public String getPosition() { return position; }

}
