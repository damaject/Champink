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

    User findByUsername(String username);
    List<User> findUserByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search1, String search2, String search3, Sort sort);

}
