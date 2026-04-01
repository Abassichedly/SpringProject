package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.ActivityTemplate;

import java.util.List;

@Repository
public interface ActivityTemplateRepository extends JpaRepository<ActivityTemplate, Long> {

    List<ActivityTemplate> findByType(String type);

    List<ActivityTemplate> findByTypeAndPopulariteGreaterThan(String type, Integer popularite);

    @Query("SELECT t FROM ActivityTemplate t WHERE t.tags LIKE %:tag%")
    List<ActivityTemplate> findByTagContaining(@Param("tag") String tag);

    @Query("SELECT t FROM ActivityTemplate t WHERE t.niveau = :niveau ORDER BY t.popularite DESC")
    List<ActivityTemplate> findByNiveauOrderByPopulariteDesc(@Param("niveau") String niveau);

    @Query("SELECT t FROM ActivityTemplate t WHERE t.actif = true")
    List<ActivityTemplate> findAllActive();
}