package ink.champ.repository;

import ink.champ.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long>, JpaRepository<Player, Long> { }
