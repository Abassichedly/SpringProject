package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.ARMP.enums.StatutEvent;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // ========== RECHERCHE SIMPLE PAR ATTRIBUT ==========
    List<Event> findByNomContaining(String nom);
    List<Event> findByLieuContaining(String lieu);
    List<Event> findByStatut(StatutEvent statut);

    // CORRIGÉ : findByClubIdClub
    List<Event> findByClubIdClub(Long clubId);

    List<Event> findByDateDebutAfter(LocalDateTime date);
    List<Event> findByDateDebutBefore(LocalDateTime date);
    List<Event> findByPrixAdherentLessThan(Double prix);

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @Query("SELECT e FROM Event e WHERE " +
            "(:nom IS NULL OR e.nom LIKE %:nom%) AND " +
            "(:lieu IS NULL OR e.lieu LIKE %:lieu%) AND " +
            "(:statut IS NULL OR e.statut = :statut)")
    List<Event> searchEvents(@Param("nom") String nom,
                             @Param("lieu") String lieu,
                             @Param("statut") StatutEvent statut);

    // CORRIGÉ : Utilisation de club.idClub
    @Query("SELECT e FROM Event e WHERE " +
            "(:nom IS NULL OR e.nom LIKE %:nom%) AND " +
            "(:lieu IS NULL OR e.lieu LIKE %:lieu%) AND " +
            "(:statut IS NULL OR e.statut = :statut) AND " +
            "(:clubId IS NULL OR e.club.idClub = :clubId) AND " +
            "(:dateStart IS NULL OR e.dateDebut >= :dateStart) AND " +
            "(:dateEnd IS NULL OR e.dateDebut <= :dateEnd) AND " +
            "(:prixMax IS NULL OR e.prixAdherent <= :prixMax)")
    List<Event> searchAll(@Param("nom") String nom,
                          @Param("lieu") String lieu,
                          @Param("statut") StatutEvent statut,
                          @Param("clubId") Long clubId,
                          @Param("dateStart") LocalDateTime dateStart,
                          @Param("dateEnd") LocalDateTime dateEnd,
                          @Param("prixMax") Double prixMax);

    // ========== TRI ==========
    List<Event> findAllByOrderByDateDebutAsc();
}
