package ink.champ.repository;

import ink.champ.models.ChampEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице событий чемпионатов
 * @author Maxim
 */
public interface ChampEventRepository extends JpaRepository<ChampEvent, Long> { }
