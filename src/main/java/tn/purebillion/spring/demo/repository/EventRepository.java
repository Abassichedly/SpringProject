package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}