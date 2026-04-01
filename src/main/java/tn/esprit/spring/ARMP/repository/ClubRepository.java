package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.Club;
import tn.esprit.spring.ARMP.enums.DomaineClub;
import tn.esprit.spring.ARMP.enums.StatutClub;

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
}