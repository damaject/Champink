package ink.champ.models;

import javax.persistence.*;

/**
 * Класс-модель для представления сущности вида спорта
 * @author Maxim
 */
@Entity(name="sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    /**
     * Конструктор вида спорта
     */
    public Sport() { }

    /**
     * Конструктор вида спорта
     * @param name Название
     */
    public Sport(String name) {
        this.name = name;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки названия
     * @param name Название
     */
    public void setName(String name) { this.name = name; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения названия
     * @return Названия
     */
    public String getName() { return name; }

}
