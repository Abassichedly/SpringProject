package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.Participation;
import tn.esprit.spring.ARMP.enums.StatutPresence;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByStatutPresence(StatutPresence statutPresence);

    @Query("SELECT p FROM Participation p WHERE p.userId = :userId")
    List<Participation> findByUserId(@Param("userId") String userId);

    @Query("SELECT p FROM Participation p WHERE p.activite.idActivite = :activiteId")
    List<Participation> findByActiviteId(@Param("activiteId") Long activiteId);

    @Query("SELECT p FROM Participation p WHERE p.event.idEvent = :eventId")
    List<Participation> findByEventId(@Param("eventId") Long eventId);

    @Query("SELECT p FROM Participation p WHERE " +
            "(:userId IS NULL OR p.userId = :userId) AND " +
            "(:statutPresence IS NULL OR p.statutPresence = :statutPresence)")
    List<Participation> searchParticipations(@Param("userId") String userId,
                                             @Param("statutPresence") StatutPresence statutPresence);

    @Query("SELECT p FROM Participation p WHERE " +
            "(:userId IS NULL OR p.userId = :userId) AND " +
            "(:activiteId IS NULL OR p.activite.idActivite = :activiteId) AND " +
            "(:eventId IS NULL OR p.event.idEvent = :eventId) AND " +
            "(:statutPresence IS NULL OR p.statutPresence = :statutPresence) AND " +
            "(:dateStart IS NULL OR p.dateInscription >= :dateStart) AND " +
            "(:dateEnd IS NULL OR p.dateInscription <= :dateEnd)")
    List<Participation> searchAll(@Param("userId") String userId,
                                  @Param("activiteId") Long activiteId,
                                  @Param("eventId") Long eventId,
                                  @Param("statutPresence") StatutPresence statutPresence,
                                  @Param("dateStart") LocalDate dateStart,
                                  @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT p FROM Participation p WHERE p.userId = :userId AND p.activite.idActivite = :activiteId")
    List<Participation> findByUserIdAndActiviteId(@Param("userId") String userId, @Param("activiteId") Long activiteId);

    @Query("SELECT p FROM Participation p WHERE p.userId = :userId AND p.event.idEvent = :eventId")
    List<Participation> findByUserIdAndEventId(@Param("userId") String userId, @Param("eventId") Long eventId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.activite.idActivite = :activiteId AND p.statutPresence = 'PRESENT'")
    long countPresencesByActiviteId(@Param("activiteId") Long activiteId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.event.idEvent = :eventId AND p.statutPresence = 'PRESENT'")
    long countPresencesByEventId(@Param("eventId") Long eventId);

    @Query("SELECT p.userId, COUNT(p) FROM Participation p GROUP BY p.userId ORDER BY COUNT(p) DESC")
    List<Object[]> findTopActiveUsers();

    @Query("SELECT YEAR(p.dateInscription), MONTH(p.dateInscription), COUNT(p) FROM Participation p " +
            "WHERE p.dateInscription BETWEEN :start AND :end " +
            "GROUP BY YEAR(p.dateInscription), MONTH(p.dateInscription) ORDER BY 1, 2")
    List<Object[]> findParticipationTrends(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT DAYOFWEEK(p.dateInscription), COUNT(p) FROM Participation p GROUP BY DAYOFWEEK(p.dateInscription)")
    List<Object[]> getActivityHeatmap();
}