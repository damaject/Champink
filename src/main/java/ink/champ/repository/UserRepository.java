package ink.champ.repository;

import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Интерфейс-репозиторий для работы с базой данных в таблице пользователей
 * @author Maxim
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Метод для получения пользователя по имени
     * @param username Имя пользователя
     * @return Пользователь
     */
    User findByUsername(String username);

    /**
     * Метод для получения отсортированного списка игроков с фильтром по имени, логину и адресу электронной почты
     * @param search1 Часть имени для поиска
     * @param search2 Часть логина для поиска
     * @param search3 Часть адреса электронной почты для поиска
     * @param sort Сортировка
     * @return Список пользователей
     */
    List<User> findUserByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search1, String search2, String search3, Sort sort);

}
