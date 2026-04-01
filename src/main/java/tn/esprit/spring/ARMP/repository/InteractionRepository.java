package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.Interaction;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    @Query("SELECT i FROM Interaction i WHERE i.userSource.id = :userId")
    List<Interaction> findByUserSourceId(@Param("userId") String userId);

    @Query("SELECT i FROM Interaction i WHERE i.userCible.id = :userId")
    List<Interaction> findByUserCibleId(@Param("userId") String userId);

    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.userCible.id = :userId")
    int countByUserCibleId(@Param("userId") String userId);

    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.userSource.id = :userId")
    int countByUserSourceId(@Param("userId") String userId);

    @Query("SELECT i.userSource, COUNT(i) FROM Interaction i GROUP BY i.userSource ORDER BY COUNT(i) DESC")
    List<Object[]> findTopActiveUsers();
}