package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.enums.DomaineClub;
import tn.purebillion.spring.demo.enums.StatutClub;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findByNomContaining(String nom);
    List<Club> findByDomaine(DomaineClub domaine);
    List<Club> findByStatut(StatutClub statut);
    List<Club> findByDateCreationBetween(LocalDate start, LocalDate end);

    @Query("SELECT c FROM Club c WHERE " +
            "(:nom IS NULL OR c.nom LIKE %:nom%) AND " +
            "(:domaine IS NULL OR c.domaine = :domaine) AND " +
            "(:statut IS NULL OR c.statut = :statut)")
    List<Club> searchClubs(@Param("nom") String nom,
                           @Param("domaine") DomaineClub domaine,
                           @Param("statut") StatutClub statut);

    @Query("SELECT c FROM Club c WHERE " +
            "(:nom IS NULL OR c.nom LIKE %:nom%) AND " +
            "(:sigle IS NULL OR c.sigle LIKE %:sigle%) AND " +
            "(:description IS NULL OR c.description LIKE %:description%) AND " +
            "(:domaine IS NULL OR c.domaine = :domaine) AND " +
            "(:statut IS NULL OR c.statut = :statut) AND " +
            "(:dateStart IS NULL OR c.dateCreation >= :dateStart) AND " +
            "(:dateEnd IS NULL OR c.dateCreation <= :dateEnd)")
    List<Club> searchAll(@Param("nom") String nom,
                         @Param("sigle") String sigle,
                         @Param("description") String description,
                         @Param("domaine") DomaineClub domaine,
                         @Param("statut") StatutClub statut,
                         @Param("dateStart") LocalDate dateStart,
                         @Param("dateEnd") LocalDate dateEnd);

    List<Club> findAllByOrderByNomAsc();
    List<Club> findAllByOrderByDateCreationDesc();

    // AJOUTER CES MÉTHODES POUR LES PRÉDICTIONS
    @Query("SELECT c, SIZE(c.membres), (SELECT COUNT(p) FROM Participation p WHERE p.activite.club = c) FROM Club c WHERE c.idClub = :clubId")
    List<Object[]> predictClubGrowth(@Param("clubId") Long clubId);

    @Query("SELECT c, (SELECT COUNT(m) FROM Membre m WHERE m.club = c AND m.estActif = true), (SELECT COUNT(m) FROM Membre m WHERE m.club = c) FROM Club c")
    List<Object[]> findClubsByRetentionRate();

    @Query("SELECT c FROM Club c WHERE (SELECT COUNT(p) FROM Participation p WHERE p.activite.club = c AND p.dateInscription > :date) < (SELECT COUNT(p) FROM Participation p WHERE p.activite.club = c AND p.dateInscription <= :date)")
    List<Club> findClubsInDanger(@Param("date") LocalDate date);

    @Query("SELECT c2 FROM Club c1, Club c2 WHERE c1.idClub = :clubId AND c2.idClub != :clubId AND c1.domaine = c2.domaine ORDER BY SIZE(c2.membres) DESC")
    List<Club> findSimilarClubs(@Param("clubId") Long clubId);

    @Query("SELECT c, COUNT(p) FROM Club c LEFT JOIN Participation p ON p.activite.club = c GROUP BY c ORDER BY COUNT(p) DESC")
    List<Object[]> findClubsByPerformance();
}