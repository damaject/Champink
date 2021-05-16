package ink.champ.repository;

import ink.champ.models.Player;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице игроков
 * @author Maxim
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Метод для получения отсортированного списка игроков с фильтром по имени
     * @param search Часть имени для поиска
     * @param sort Сортировка
     * @return Список игроков
     */
    List<Player> findPlayersByNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка публичных игроков с фильтром по имени
     * @param search Часть имени для поиска
     * @param sort Сортировка
     * @return Список игроков
     */
    List<Player> findPlayersByPrivatIsFalseAndNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка пользовательских игроков с фильтром по названию
     * @param user Пользователь
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    @Query("SELECT p FROM players p INNER JOIN p.roles pr WHERE pr.user = ?1 AND pr.role > 0 AND lower(p.name) LIKE lower(concat('%', ?2, '%')) ORDER BY pr.role DESC, p.id DESC")
    List<Player> findPlayersByUserAll(User user, String search);

    /**
     * Метод для получения отсортированного списка пользовательских игроков по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть имени для поиска
     * @return Список игроков
     */
    @Query("SELECT p FROM players p INNER JOIN p.roles pr WHERE pr.user = ?1 AND pr.role = ?2 AND lower(p.name) LIKE lower(concat('%', ?3, '%')) ORDER BY p.id DESC")
    List<Player> findPlayersByUserRole(User user, int role, String search);

    /**
     * Метод для получения списка пользовательских игроков которые не состоят в команде
     * @param team Команда
     * @param user Пользователь
     * @return Список игроков
     */
    @Query("SELECT p FROM players p INNER JOIN p.roles pr WHERE (SELECT COUNT(pt.id) FROM p.teams pt WHERE pt.team = ?1) = 0 AND pr.user = ?2 AND pr.role >= 3 ORDER BY p.id DESC")
    List<Player> findPlayersByUserRoleAndNotInTeam(Team team, User user);

}
