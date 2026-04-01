package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.ARMP.entity.Activite;
import tn.esprit.spring.ARMP.enums.StatutActivite;
import tn.esprit.spring.ARMP.enums.TypeActivite;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    // ========== RECHERCHE SIMPLE PAR ATTRIBUT ==========
    List<Activite> findByTitreContaining(String titre);
    List<Activite> findByType(TypeActivite type);
    List<Activite> findByLieuContaining(String lieu);
    List<Activite> findByStatut(StatutActivite statut);

    // CORRIGÉ : findByClubId au lieu de findByClubId
    List<Activite> findByClubIdClub(Long clubId);

    List<Activite> findByDate(LocalDate date);
    List<Activite> findByDateAfter(LocalDate date);
    List<Activite> findByDateBefore(LocalDate date);

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @Query("SELECT a FROM Activite a WHERE " +
            "(:titre IS NULL OR a.titre LIKE %:titre%) AND " +
            "(:type IS NULL OR a.type = :type) AND " +
            "(:lieu IS NULL OR a.lieu LIKE %:lieu%)")
    List<Activite> searchActivites(@Param("titre") String titre,
                                   @Param("type") TypeActivite type,
                                   @Param("lieu") String lieu);

    // CORRIGÉ : Utilisation de club.idClub
    @Query("SELECT a FROM Activite a WHERE " +
            "(:titre IS NULL OR a.titre LIKE %:titre%) AND " +
            "(:type IS NULL OR a.type = :type) AND " +
            "(:lieu IS NULL OR a.lieu LIKE %:lieu%) AND " +
            "(:statut IS NULL OR a.statut = :statut) AND " +
            "(:clubId IS NULL OR a.club.idClub = :clubId) AND " +
            "(:dateStart IS NULL OR a.date >= :dateStart) AND " +
            "(:dateEnd IS NULL OR a.date <= :dateEnd)")
    List<Activite> searchAll(@Param("titre") String titre,
                             @Param("type") TypeActivite type,
                             @Param("lieu") String lieu,
                             @Param("statut") StatutActivite statut,
                             @Param("clubId") Long clubId,
                             @Param("dateStart") LocalDate dateStart,
                             @Param("dateEnd") LocalDate dateEnd);

    // ========== TRI ==========
    List<Activite> findAllByOrderByDateAsc();

    // CORRIGÉ : Utilisation de club.idClub
    @Query("SELECT a FROM Activite a WHERE a.club.idClub = :clubId ORDER BY a.date DESC")
    List<Activite> findByClubIdOrderByDateDesc(@Param("clubId") Long clubId);

    @Query("SELECT a FROM Activite a WHERE a.club.idClub = :clubId")
    List<Activite> findByClubId(@Param("clubId") Long clubId);
}