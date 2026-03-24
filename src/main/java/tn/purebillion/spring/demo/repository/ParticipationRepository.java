package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.enums.StatutPresence;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    // ========== RECHERCHES EXISTANTES ==========
    List<Participation> findByStatutPresence(StatutPresence statutPresence);
    List<Participation> findByMembreIdMembre(Long membreId);
    List<Participation> findByActiviteIdActivite(Long activiteId);
    List<Participation> findByEventIdEvent(Long eventId);

    @Query("SELECT p FROM Participation p WHERE " +
            "(:membreId IS NULL OR p.membre.idMembre = :membreId) AND " +
            "(:statutPresence IS NULL OR p.statutPresence = :statutPresence)")
    List<Participation> searchParticipations(@Param("membreId") Long membreId,
                                             @Param("statutPresence") StatutPresence statutPresence);

    @Query("SELECT p FROM Participation p WHERE " +
            "(:membreId IS NULL OR p.membre.idMembre = :membreId) AND " +
            "(:activiteId IS NULL OR p.activite.idActivite = :activiteId) AND " +
            "(:eventId IS NULL OR p.event.idEvent = :eventId) AND " +
            "(:statutPresence IS NULL OR p.statutPresence = :statutPresence) AND " +
            "(:dateStart IS NULL OR p.dateInscription >= :dateStart) AND " +
            "(:dateEnd IS NULL OR p.dateInscription <= :dateEnd)")
    List<Participation> searchAll(@Param("membreId") Long membreId,
                                  @Param("activiteId") Long activiteId,
                                  @Param("eventId") Long eventId,
                                  @Param("statutPresence") StatutPresence statutPresence,
                                  @Param("dateStart") LocalDate dateStart,
                                  @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT p FROM Participation p WHERE p.membre.idMembre = :membreId AND p.activite.idActivite = :activiteId")
    List<Participation> findByMembreIdAndActiviteId(@Param("membreId") Long membreId, @Param("activiteId") Long activiteId);

    @Query("SELECT p FROM Participation p WHERE p.membre.idMembre = :membreId AND p.event.idEvent = :eventId")
    List<Participation> findByMembreIdAndEventId(@Param("membreId") Long membreId, @Param("eventId") Long eventId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.activite.idActivite = :activiteId AND p.statutPresence = 'PRESENT'")
    long countPresencesByActiviteId(@Param("activiteId") Long activiteId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.event.idEvent = :eventId AND p.statutPresence = 'PRESENT'")
    long countPresencesByEventId(@Param("eventId") Long eventId);

    // AJOUTER CES MÉTHODES POUR L'ANALYSE
    @Query("SELECT p.membre, COUNT(p) FROM Participation p GROUP BY p.membre ORDER BY COUNT(p) DESC")
    List<Object[]> findTopActiveMembres();

    @Query("SELECT YEAR(p.dateInscription), MONTH(p.dateInscription), COUNT(p) FROM Participation p " +
            "WHERE p.dateInscription BETWEEN :start AND :end " +
            "GROUP BY YEAR(p.dateInscription), MONTH(p.dateInscription) ORDER BY 1, 2")
    List<Object[]> findParticipationTrends(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT DAYOFWEEK(p.dateInscription), COUNT(p) FROM Participation p GROUP BY DAYOFWEEK(p.dateInscription)")
    List<Object[]> getActivityHeatmap();
}