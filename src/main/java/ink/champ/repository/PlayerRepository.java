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

    @Query("SELECT p FROM players p WHERE p.user = ?1")
    List<Player> findPlayersByUserAndNotInTeam(User user, Team team);

}
