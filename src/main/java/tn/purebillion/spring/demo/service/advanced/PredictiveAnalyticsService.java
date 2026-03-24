package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.entity.PredictionScore;
import tn.purebillion.spring.demo.repository.ClubRepository;
import tn.purebillion.spring.demo.repository.PredictionScoreRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PredictiveAnalyticsService {

    private final ClubRepository clubRepository;
    private final PredictionScoreRepository predictionRepository;

    // 1. Prédire l'évolution d'un club (avec sauvegarde)
    public Map<String, Object> predictClubEvolution(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Club non trouvé");
            return error;
        }

        int currentMembres = club.getMembres().size();
        int currentActivites = club.getActivites().size();

        // Calcul du taux de croissance
        double growthRate = (currentActivites > 0) ?
                (currentMembres * 0.1) + (currentActivites * 0.05) : 0.05;

        int predictedMembres3Mois = currentMembres + (int)(currentMembres * growthRate);
        int predictedMembres6Mois = predictedMembres3Mois + (int)(predictedMembres3Mois * growthRate * 0.8);
        int predictedMembres12Mois = predictedMembres6Mois + (int)(predictedMembres6Mois * growthRate * 0.6);

        double confidence = Math.min(0.95, 0.6 + (currentActivites * 0.05));

        String prediction = (predictedMembres12Mois > currentMembres * 2) ? "🚀 CROISSANCE EXPONENTIELLE" :
                (predictedMembres12Mois > currentMembres) ? "📈 CROISSANCE MODÉRÉE" :
                        "📉 STAGNATION";

        // Sauvegarder la prédiction
        PredictionScore score = PredictionScore.builder()
                .club(club)
                .type("CROISSANCE")
                .score((double) predictedMembres12Mois)
                .confiance(confidence)
                .facteurs(String.format("Membres: %d, Activités: %d, Taux: %.2f%%",
                        currentMembres, currentActivites, growthRate * 100))
                .datePrediction(LocalDateTime.now())
                .build();
        predictionRepository.save(score);

        // FIX: Utiliser HashMap au lieu de Map.of()
        Map<String, Object> result = new HashMap<>();
        result.put("club", club.getNom());
        result.put("currentMembres", currentMembres);
        result.put("predictedMembres3Mois", predictedMembres3Mois);
        result.put("predictedMembres6Mois", predictedMembres6Mois);
        result.put("predictedMembres12Mois", predictedMembres12Mois);
        result.put("tauxCroissance", String.format("%.1f%%", growthRate * 100));
        result.put("confiance", String.format("%.1f%%", confidence * 100));
        result.put("prediction", prediction);
        result.put("recommendation", getRecommendation(growthRate));

        return result;
    }

    private String getRecommendation(double growthRate) {
        if (growthRate > 0.3) return "Excellent ! Continuez vos activités actuelles";
        if (growthRate > 0.15) return "Bien, augmentez la fréquence des activités";
        if (growthRate > 0.05) return "Peut mieux faire, communiquez plus";
        return "Urgent : relancez vos activités !";
    }

    // 2. Prédire le risque de départ d'un membre
    public Map<String, Object> predictChurnRisk(Long membreId) {
        // FIX: Utiliser HashMap au lieu de Map.of()
        Map<String, Object> result = new HashMap<>();
        result.put("membreId", membreId);
        result.put("riskScore", 0.35);
        result.put("riskLevel", "MOYEN");
        result.put("recommendation", "Envoyez des notifications d'activités");
        return result;
    }

    // 3. Prédire les tendances d'activités
    public Map<String, Object> predictActivityTrends() {
        List<PredictionScore> recentPredictions = predictionRepository.findLatestByType("CROISSANCE");

        // FIX: Utiliser HashMap au lieu de Map.of()
        Map<String, Object> trends = new HashMap<>();
        trends.put("periode", "6 prochains mois");
        trends.put("tendanceGenerale", "HAUSSE");

        List<String> facteurs = new ArrayList<>();
        facteurs.add("Augmentation des inscriptions");
        facteurs.add("Nouveaux clubs créés");
        facteurs.add("Activités plus variées");
        trends.put("facteurs", facteurs);

        return trends;
    }

    // 4. Classement des clubs par potentiel
    public List<Map<String, Object>> rankClubsByPotential() {
        List<Club> clubs = clubRepository.findAll();
        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Club club : clubs) {
            int membres = club.getMembres().size();
            int activites = club.getActivites().size();
            double potential = (membres * 0.6) + (activites * 0.4);

            Map<String, Object> clubRank = new HashMap<>();
            clubRank.put("club", club.getNom());
            clubRank.put("membres", membres);
            clubRank.put("activites", activites);
            clubRank.put("potentialScore", Math.round(potential * 100) / 100.0);
            ranking.add(clubRank);
        }

        ranking.sort((a, b) -> Double.compare(
                (double) b.get("potentialScore"), (double) a.get("potentialScore")));

        return ranking;
    }

    // 5. Historique des prédictions
    public List<Map<String, Object>> getPredictionHistory(Long clubId) {
        List<PredictionScore> predictions = predictionRepository.findByClubId(clubId);

        List<Map<String, Object>> history = new ArrayList<>();

        for (PredictionScore p : predictions) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", p.getDatePrediction());
            item.put("type", p.getType());
            item.put("score", p.getScore());
            item.put("confiance", p.getConfiance());
            item.put("facteurs", p.getFacteurs());
            history.add(item);
        }

        return history;
    }
}