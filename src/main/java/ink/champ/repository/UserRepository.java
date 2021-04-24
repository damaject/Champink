package ink.champ.repository;

import ink.champ.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User, Long> {

    User findByUsername(String username);

}
