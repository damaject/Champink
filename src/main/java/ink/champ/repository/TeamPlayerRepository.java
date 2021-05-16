package ink.champ.repository;

import ink.champ.models.TeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице игроков команд
 * @author Maxim
 */
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> { }
