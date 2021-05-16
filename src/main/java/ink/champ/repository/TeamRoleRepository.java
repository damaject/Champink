package ink.champ.repository;

import ink.champ.models.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице пользовательских ролей команд
 * @author Maxim
 */
public interface TeamRoleRepository extends JpaRepository<TeamRole, Long> { }
