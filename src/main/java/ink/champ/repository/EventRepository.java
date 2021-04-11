package ink.champ.repository;

import ink.champ.models.Event;
import ink.champ.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long>, JpaRepository<Event, Long> { }
