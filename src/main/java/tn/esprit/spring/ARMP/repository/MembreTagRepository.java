package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.MembreTag;
import tn.esprit.spring.ARMP.entity.User;

import java.util.List;

@Repository
public interface MembreTagRepository extends JpaRepository<MembreTag, Long> {

    @Query("SELECT t FROM MembreTag t WHERE t.user.id = :userId")
    List<MembreTag> findByUserId(@Param("userId") String userId);

    @Query("SELECT t FROM MembreTag t WHERE t.user.id = :userId")
    List<MembreTag> findTagsByUserId(@Param("userId") String userId);

    @Query("SELECT u FROM User u WHERE EXISTS (SELECT t FROM MembreTag t WHERE t.user = u AND t.tag = :tag)")
    List<User> findUsersByTag(@Param("tag") String tag);

    @Query("SELECT u FROM User u WHERE EXISTS (SELECT t FROM MembreTag t WHERE t.user = u AND t.tag IN :tags)")
    List<User> findUsersByTags(@Param("tags") List<String> tags);

    @Query("SELECT t.tag, COUNT(t) FROM MembreTag t GROUP BY t.tag ORDER BY COUNT(t) DESC")
    List<Object[]> findMostPopularTags();
}