package tn.purebillion.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.MembreTag;

import java.util.List;

@Repository
public interface MembreTagRepository extends JpaRepository<MembreTag, Long> {

    @Query("SELECT t FROM MembreTag t WHERE t.membre.idMembre = :membreId")
    List<MembreTag> findByMembreId(@Param("membreId") Long membreId);

    @Query("SELECT t FROM MembreTag t WHERE t.membre.idMembre = :membreId")
    List<MembreTag> findTagsByMembreId(@Param("membreId") Long membreId);

    @Query("SELECT m FROM Membre m WHERE EXISTS (SELECT t FROM MembreTag t WHERE t.membre = m AND t.tag = :tag)")
    List<Membre> findMembresByTag(@Param("tag") String tag);

    @Query("SELECT m FROM Membre m WHERE EXISTS (SELECT t FROM MembreTag t WHERE t.membre = m AND t.tag IN :tags)")
    List<Membre> findMembresByTags(@Param("tags") List<String> tags);

    @Query("SELECT t.tag, COUNT(t) FROM MembreTag t GROUP BY t.tag ORDER BY COUNT(t) DESC")
    List<Object[]> findMostPopularTags();

    @Query("SELECT DISTINCT t.tag FROM MembreTag t")
    List<String> findAllTags();
}