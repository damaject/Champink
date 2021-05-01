package ink.champ.repository;

import ink.champ.models.Champ;
import ink.champ.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChampRepository extends JpaRepository<Champ, Long> {

    List<Champ> findChampsByUser(User user);
    List<Champ> findChampsByUser(User user, Sort sort);

}
