package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Membre;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {
}