package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChampRepository extends JpaRepository<Champ, Long> {

    List<Champ> findChampsByUser(User user);
    List<Champ> findChampsByUser(User user, Sort sort);
    List<Champ> findChampsByPrivatIsFalse(Sort sort);

    @Query("SELECT c FROM champs c INNER JOIN c.roles cr WHERE cr.user = ?1 AND cr.role > 0 ORDER BY cr.role DESC, c.id DESC")
    List<Champ> findChampsByUserAll(User user);

    @Query("SELECT c FROM champs c INNER JOIN c.roles cr WHERE cr.user = ?1 AND cr.role = ?2 ORDER BY c.id DESC")
    List<Champ> findChampsByUserRole(User user, int role);
}
