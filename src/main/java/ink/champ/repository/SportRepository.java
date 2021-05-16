package ink.champ.repository;

import ink.champ.models.Sport;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице видов спорта
 * @author Maxim
 */
public interface SportRepository extends JpaRepository<Sport, Long> {

    List<Sport> findSportByNameContainingIgnoreCase(String search, Sort sort);

}
