package ink.champ.repository;

import ink.champ.models.Champ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ChampRepository extends CrudRepository<Champ, Long>, JpaRepository<Champ, Long> { }
