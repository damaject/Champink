package ink.champ.repository;

import ink.champ.models.Player;
import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findPlayersByUser(User user);
    List<Player> findPlayersByUser(User user, Sort sort);
    List<Player> findPlayersByPrivatIsFalse(Sort sort);

    @Query("SELECT p FROM players p INNER JOIN p.roles pr WHERE pr.user = ?1 AND pr.role > 0 ORDER BY pr.role DESC, p.id DESC")
    List<Player> findPlayersByUserAll(User user);

    @Query("SELECT p FROM players p INNER JOIN p.roles pr WHERE pr.user = ?1 AND pr.role = ?2 ORDER BY p.id DESC")
    List<Player> findPlayersByUserRole(User user, int role);

}
