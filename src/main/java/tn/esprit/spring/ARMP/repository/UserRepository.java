package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.enums.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // ========== RECHERCHES POUR LA GESTION DES CLUBS ==========
    List<User> findByFirstNameContaining(String firstName);
    List<User> findByLastNameContaining(String lastName);
    List<User> findByEmailContaining(String email);
    List<User> findByRole(UserRole role);
    List<User> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM User u WHERE u.club.idClub = :clubId")
    List<User> findByClubId(@Param("clubId") Long clubId);

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:role IS NULL OR u.role = :role)")
    List<User> searchUsers(@Param("firstName") String firstName,
                           @Param("lastName") String lastName,
                           @Param("role") UserRole role);

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:isActive IS NULL OR u.isActive = :isActive) AND " +
            "(:clubId IS NULL OR u.club.idClub = :clubId) AND " +
            "(:dateStart IS NULL OR u.dateAdhesion >= :dateStart) AND " +
            "(:dateEnd IS NULL OR u.dateAdhesion <= :dateEnd)")
    List<User> searchAll(@Param("firstName") String firstName,
                         @Param("lastName") String lastName,
                         @Param("email") String email,
                         @Param("role") UserRole role,
                         @Param("isActive") Boolean isActive,
                         @Param("clubId") Long clubId,
                         @Param("dateStart") LocalDate dateStart,
                         @Param("dateEnd") LocalDate dateEnd);

    List<User> findAllByOrderByFirstNameAsc();
    List<User> findAllByOrderByDateAdhesionDesc();

    @Query("SELECT u FROM User u WHERE u.club.idClub = :clubId AND u.isActive = true")
    List<User> findByClubIdAndIsActiveTrue(@Param("clubId") Long clubId);

    @Query("SELECT u FROM User u WHERE u.club.idClub = :clubId AND u.role = :role")
    List<User> findByClubIdAndRole(@Param("clubId") Long clubId, @Param("role") UserRole role);

    @Query("SELECT u FROM User u WHERE (SELECT COUNT(p) FROM Participation p WHERE p.userId = u.id) >= :minParticipations")
    List<User> findUsersWithAtLeastNParticipations(@Param("minParticipations") int minParticipations);

    @Query("SELECT DISTINCT u2 FROM User u1, User u2 " +
            "WHERE u1.id = :userId AND u2.id != :userId " +
            "AND u1.club = u2.club")
    List<User> findConnectedUsers(@Param("userId") String userId);
}