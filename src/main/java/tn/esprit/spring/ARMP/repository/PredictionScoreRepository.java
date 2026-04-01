package tn.esprit.spring.ARMP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.ARMP.entity.PredictionScore;

import java.util.List;

@Repository
public interface PredictionScoreRepository extends JpaRepository<PredictionScore, Long> {

    // Trouver les prédictions d'un club
    @Query("SELECT p FROM PredictionScore p WHERE p.club.idClub = :clubId ORDER BY p.datePrediction DESC")
    List<PredictionScore> findByClubId(@Param("clubId") Long clubId);

    // Trouver les dernières prédictions par type
    @Query("SELECT p FROM PredictionScore p WHERE p.type = :type ORDER BY p.datePrediction DESC")
    List<PredictionScore> findLatestByType(@Param("type") String type);

    // Trouver les prédictions avec une confiance élevée
    @Query("SELECT p FROM PredictionScore p WHERE p.confiance > :minConfiance ORDER BY p.confiance DESC")
    List<PredictionScore> findByHighConfidence(@Param("minConfiance") Double minConfiance);

    // Trouver les prédictions non vérifiées
    @Query("SELECT p FROM PredictionScore p WHERE p.estVerifiee = false ORDER BY p.datePrediction ASC")
    List<PredictionScore> findUnverifiedPredictions();

    // Score moyen par type de prédiction
    @Query("SELECT p.type, AVG(p.score) FROM PredictionScore p GROUP BY p.type")
    List<Object[]> findAverageScoreByType();
}