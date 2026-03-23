package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.enums.RoleMembre;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {
    // ========== RECHERCHE SIMPLE PAR ATTRIBUT ==========
    List<Membre> findByNomContaining(String nom);
    List<Membre> findByPrenomContaining(String prenom);
    List<Membre> findByEmailContaining(String email);
    List<Membre> findByRole(RoleMembre role);
    List<Membre> findByEstActif(Boolean estActif);

    // CORRIGÉ : findByClubIdClub
    List<Membre> findByClubIdClub(Long clubId);

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @Query("SELECT m FROM Membre m WHERE " +
            "(:nom IS NULL OR m.nom LIKE %:nom%) AND " +
            "(:prenom IS NULL OR m.prenom LIKE %:prenom%) AND " +
            "(:role IS NULL OR m.role = :role)")
    List<Membre> searchMembres(@Param("nom") String nom,
                               @Param("prenom") String prenom,
                               @Param("role") RoleMembre role);

    // CORRIGÉ : Utilisation de club.idClub
    @Query("SELECT m FROM Membre m WHERE " +
            "(:nom IS NULL OR m.nom LIKE %:nom%) AND " +
            "(:prenom IS NULL OR m.prenom LIKE %:prenom%) AND " +
            "(:email IS NULL OR m.email LIKE %:email%) AND " +
            "(:role IS NULL OR m.role = :role) AND " +
            "(:estActif IS NULL OR m.estActif = :estActif) AND " +
            "(:clubId IS NULL OR m.club.idClub = :clubId) AND " +
            "(:dateStart IS NULL OR m.dateAdhesion >= :dateStart) AND " +
            "(:dateEnd IS NULL OR m.dateAdhesion <= :dateEnd)")
    List<Membre> searchAll(@Param("nom") String nom,
                           @Param("prenom") String prenom,
                           @Param("email") String email,
                           @Param("role") RoleMembre role,
                           @Param("estActif") Boolean estActif,
                           @Param("clubId") Long clubId,
                           @Param("dateStart") LocalDate dateStart,
                           @Param("dateEnd") LocalDate dateEnd);

    // ========== TRI ==========
    List<Membre> findAllByOrderByNomAsc();
    List<Membre> findAllByOrderByDateAdhesionDesc();

    // ========== RECHERCHE SPÉCIFIQUE ==========
    // CORRIGÉ : Utilisation de club.idClub
    @Query("SELECT m FROM Membre m WHERE m.club.idClub = :clubId AND m.estActif = true")
    List<Membre> findByClubIdAndEstActifTrue(@Param("clubId") Long clubId);

    @Query("SELECT m FROM Membre m WHERE m.club.idClub = :clubId AND m.role = :role")
    List<Membre> findByClubIdAndRole(@Param("clubId") Long clubId, @Param("role") RoleMembre role);

    @Query("SELECT m FROM Membre m WHERE (SELECT COUNT(p) FROM Participation p WHERE p.membre = m) >= :minParticipations")
    List<Membre> findMembresWithAtLeastNParticipations(@Param("minParticipations") int minParticipations);

}