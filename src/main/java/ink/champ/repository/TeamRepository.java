package ink.champ.repository;

import ink.champ.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long>, JpaRepository<Team, Long> { }
