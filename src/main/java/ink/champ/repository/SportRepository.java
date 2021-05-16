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

    /**
     * Метод для получения отсортированного списка видов спорта с фильтром по имени
     * @param search Часть названия для поиска
     * @param sort Сортировка
     * @return Список видов спорта
     */
    List<Sport> findSportByNameContainingIgnoreCase(String search, Sort sort);

}
