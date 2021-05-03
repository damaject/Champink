package ink.champ.repository;

import ink.champ.models.Team;
import ink.champ.models.TeamPlayer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {


    List<TeamPlayer> findTeamPlayersByTeam(Team team);
    List<TeamPlayer> findTeamPlayersByTeam(Team team, Sort sort);

}
