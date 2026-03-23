package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}