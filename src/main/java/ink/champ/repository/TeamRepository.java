package ink.champ.repository;

import ink.champ.models.Team;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findTeamsByUser(User user);
    List<Team> findTeamsByUser(User user, Sort sort);

}
