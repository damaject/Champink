package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findTeamsByUser(User user);
    List<Team> findTeamsByUser(User user, Sort sort);
    List<Team> findTeamsByPrivatIsFalse(Sort sort);

    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE tr.user = ?1 AND tr.role > 0 ORDER BY tr.role DESC, t.id DESC")
    List<Team> findTeamsByUserAll(User user);

    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE tr.user = ?1 AND tr.role = ?2 ORDER BY t.id DESC")
    List<Team> findTeamsByUserRole(User user, int role);

    @Query("SELECT t FROM teams t INNER JOIN t.roles tr WHERE (SELECT COUNT(tc.id) FROM t.champs tc WHERE tc.champ = ?1) = 0 AND tr.user = ?2 AND tr.role >= 3 ORDER BY t.id DESC")
    List<Team> findTeamsByUserRoleAndNotInChamp(Champ champ, User user);
}
