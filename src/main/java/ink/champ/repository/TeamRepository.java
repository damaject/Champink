package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.Player;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице команд
 * @author Maxim
 */
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Метод для получения отсортированного списка команд с фильтром по названию
     * @param search Часть названия для поиска
     * @param sort Сортировка
     * @return Список команд
     */
    List<Team> findTeamsByNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка публичных команд с фильтром по названию
     * @param search Часть названия для поиска
     * @param sort Сортировка
     * @return Список команд
     */
    List<Team> findTeamsByPrivatIsFalseAndNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка пользовательских команд с фильтром по названию
     * @param user Пользователь
     * @param search Часть названия для поиска
     * @return Список команд
     */
    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE tr.user = ?1 AND tr.role > 0 AND lower(t.name) LIKE lower(concat('%', ?2, '%')) ORDER BY tr.role DESC, t.id DESC")
    List<Team> findTeamsByUserAll(User user, String search);

    /**
     * Метод для получения отсортированного списка пользовательских команд по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть названия для поиска
     * @return Список команд
     */
    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE tr.user = ?1 AND tr.role = ?2 AND lower(t.name) LIKE lower(concat('%', ?3, '%')) ORDER BY t.id DESC")
    List<Team> findTeamsByUserRole(User user, int role, String search);

    /**
     * Метод для получения списка пользовательских команд которые не состоят в чемпионате
     * @param champ Чемпионат
     * @param user Пользователь
     * @return Список команд
     */
    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE (SELECT COUNT(tc.id) FROM t.champs tc WHERE tc.champ = ?1) = 0 AND tr.user = ?2 AND tr.role >= 3 ORDER BY t.id DESC")
    List<Team> findTeamsByUserRoleAndNotInChamp(Champ champ, User user);

    /**
     * Метод для получения списка пользовательских команд в которых не состоит игрок
     * @param player Игрок
     * @param user Пользователь
     * @return Список команд
     */
    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE (SELECT COUNT(tp.id) FROM t.players tp WHERE tp.player = ?1) = 0 AND tr.user = ?2 AND tr.role >= 3 ORDER BY t.id DESC")
    List<Team> findTeamsByUserRoleAndPlayerNotIn(Player player, User user);

}
