package ink.champ.repository;

import ink.champ.models.ChampTeam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице команд чемпионатов
 * @author Maxim
 */
public interface ChampTeamRepository extends JpaRepository<ChampTeam, Long> { }
