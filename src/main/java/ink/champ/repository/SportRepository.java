package ink.champ.repository;

import ink.champ.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SportRepository extends CrudRepository<Sport, Long>, JpaRepository<Sport, Long> { }
