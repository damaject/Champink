package ink.champ.repository;

import ink.champ.models.PlayerRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице пользовательских ролей игроков
 * @author Maxim
 */
public interface PlayerRoleRepository extends JpaRepository<PlayerRole, Long> { }
