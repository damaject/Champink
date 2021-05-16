package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.ChampTeam;
import ink.champ.models.Team;
import ink.champ.models.TeamPlayer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице команд чемпионатов
 * @author Maxim
 */
public interface ChampTeamRepository extends JpaRepository<ChampTeam, Long> {

    List<ChampTeam> findChampTeamsByChamp(Champ champ);
    List<ChampTeam> findChampTeamsByChamp(Champ champ, Sort sort);

}
