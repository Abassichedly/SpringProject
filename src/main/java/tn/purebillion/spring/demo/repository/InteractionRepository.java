package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Interaction;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    // CORRIGÉ : Utiliser @Query explicite avec idMembre
    @Query("SELECT i FROM Interaction i WHERE i.membreSource.idMembre = :membreId")
    List<Interaction> findByMembreSourceId(@Param("membreId") Long membreId);

    @Query("SELECT i FROM Interaction i WHERE i.membreCible.idMembre = :membreId")
    List<Interaction> findByMembreCibleId(@Param("membreId") Long membreId);

    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.membreCible.idMembre = :membreId")
    int countByMembreCibleId(@Param("membreId") Long membreId);

    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.membreSource.idMembre = :membreId")
    int countByMembreSourceId(@Param("membreId") Long membreId);

    @Query("SELECT i.membreSource, COUNT(i) FROM Interaction i GROUP BY i.membreSource ORDER BY COUNT(i) DESC")
    List<Object[]> findTopActiveMembers();
}