package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице чемпионатов
 * @author Maxim
 */
public interface ChampRepository extends JpaRepository<Champ, Long> {

    /**
     * Метод для получения отсортированного списка чемпионатов с фильтром по названию
     * @param search Часть названия для поиска
     * @param sort Сортировка
     * @return Список чемпионатов
     */
    List<Champ> findChampsByNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка публичных чемпионатов с фильтром по названию
     * @param search Часть названия для поиска
     * @param sort Сортировка
     * @return Список чемпионатов
     */
    List<Champ> findChampsByPrivatIsFalseAndNameContainingIgnoreCase(String search, Sort sort);

    /**
     * Метод для получения отсортированного списка пользовательских чемпионатов с фильтром по названию
     * @param user Пользователь
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    @Query("SELECT c FROM champs c INNER JOIN c.roles cr WHERE cr.user = ?1 AND cr.role > 0 AND lower(c.name) LIKE lower(concat('%', ?2, '%')) ORDER BY cr.role DESC, c.id DESC")
    List<Champ> findChampsByUserAll(User user, String search);

    /**
     * Метод для получения отсортированного списка пользовательских чемпионатов по роли и с фильтром по названию
     * @param user Пользователь
     * @param role Роль пользователя
     * @param search Часть названия для поиска
     * @return Список чемпионатов
     */
    @Query("SELECT c FROM champs c INNER JOIN c.roles cr WHERE cr.user = ?1 AND cr.role = ?2 AND lower(c.name) LIKE lower(concat('%', ?3, '%')) ORDER BY c.id DESC")
    List<Champ> findChampsByUserRole(User user, int role, String search);

    /**
     * Метод для получения списка пользовательских чемпионатов в которых не участвует команда
     * @param team Команда
     * @param user Пользователь
     * @return Список чемпионатов
     */
    @Query("SELECT c FROM champs c INNER JOIN c.roles cr WHERE (SELECT COUNT(ct.id) FROM c.teams ct WHERE ct.team = ?1) = 0 AND cr.user = ?2 AND cr.role >= 3 ORDER BY c.id DESC")
    List<Champ> findChampsByUserRoleAndTeamNotIn(Team team, User user);

}
