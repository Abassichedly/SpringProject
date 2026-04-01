package tn.esprit.spring.ARMP.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Club;
import tn.esprit.spring.ARMP.repository.ClubRepository;
import tn.esprit.spring.ARMP.repository.MembreTagRepository;
import tn.esprit.spring.ARMP.repository.ParticipationRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class InsightAnalyticsService {

    private final ClubRepository clubRepository;
    private final ParticipationRepository participationRepository;
    private final MembreTagRepository membreTagRepository;

    // 1. Heatmap d'activité
    public Map<String, Object> getActivityHeatmap() {
        List<Object[]> heatmapData = participationRepository.getActivityHeatmap();

        Map<String, Long> dayMap = new LinkedHashMap<>();
        dayMap.put("LUNDI", 0L);
        dayMap.put("MARDI", 0L);
        dayMap.put("MERCREDI", 0L);
        dayMap.put("JEUDI", 0L);
        dayMap.put("VENDREDI", 0L);
        dayMap.put("SAMEDI", 0L);
        dayMap.put("DIMANCHE", 0L);

        for (Object[] data : heatmapData) {
            Integer day = (Integer) data[0];
            Long count = ((Number) data[1]).longValue();
            String dayName;
            switch (day) {
                case 2: dayName = "LUNDI"; break;
                case 3: dayName = "MARDI"; break;
                case 4: dayName = "MERCREDI"; break;
                case 5: dayName = "JEUDI"; break;
                case 6: dayName = "VENDREDI"; break;
                case 7: dayName = "SAMEDI"; break;
                default: dayName = "DIMANCHE";
            }
            dayMap.put(dayName, count);
        }

        String bestDay = "";
        long maxActivity = 0;
        for (Map.Entry<String, Long> entry : dayMap.entrySet()) {
            if (entry.getValue() > maxActivity) {
                maxActivity = entry.getValue();
                bestDay = entry.getKey();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("heatmap", dayMap);
        result.put("bestDay", bestDay);
        result.put("peakActivity", maxActivity);

        return result;
    }

    // 2. Rapport de santé du club
    public Map<String, Object> getClubHealthReport(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Club non trouvé");
            return error;
        }

        int membresCount = club.getUsers().size();
        int activitesCount = club.getActivites().size();
        int eventsCount = club.getEvents().size();

        double engagementRate = membresCount > 0 ?
                (double) (activitesCount + eventsCount) / membresCount : 0;

        String healthStatus;
        String icon;
        if (engagementRate > 2) {
            healthStatus = "EXCELLENTE";
            icon = "💚";
        } else if (engagementRate > 1) {
            healthStatus = "BONNE";
            icon = "💛";
        } else if (engagementRate > 0.5) {
            healthStatus = "MODÉRÉE";
            icon = "🧡";
        } else {
            healthStatus = "CRITIQUE";
            icon = "❤️";
        }

        List<String> recommendations = new ArrayList<>();
        if (engagementRate < 1) {
            recommendations.add("Organisez plus d'activités");
            recommendations.add("Communiquez davantage avec les users");
        }
        if (engagementRate < 0.5) {
            recommendations.add("Lancez un sondage pour comprendre les besoins");
            recommendations.add("Proposez des activités variées");
        }
        if (membresCount < 5) {
            recommendations.add("Recrutez de nouveaux users");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("club", club.getNom());
        result.put("healthStatus", healthStatus);
        result.put("icon", icon);
        result.put("membres", membresCount);
        result.put("activites", activitesCount);
        result.put("events", eventsCount);
        result.put("engagementRate", String.format("%.2f", engagementRate));
        result.put("recommendations", recommendations);

        return result;
    }

    // 3. Comparaison des clubs
    public List<Map<String, Object>> compareClubs() {
        List<Club> clubs = clubRepository.findAll();
        List<Map<String, Object>> comparison = new ArrayList<>();

        for (Club club : clubs) {
            int membres = club.getUsers().size();
            int activites = club.getActivites().size();

            Map<String, Object> clubStats = new HashMap<>();
            clubStats.put("club", club.getNom());
            clubStats.put("membres", membres);
            clubStats.put("activites", activites);
            clubStats.put("score", (activites * 2 + membres) / 10.0);
            clubStats.put("performance", getPerformanceIcon(activites, membres));
            comparison.add(clubStats);
        }

        comparison.sort((a, b) -> Double.compare(
                (double) b.get("score"), (double) a.get("score")));

        return comparison;
    }

    private String getPerformanceIcon(int activites, int membres) {
        if (activites > membres / 2) return "🔥 EXCELLENT";
        if (activites > membres / 4) return "👍 BON";
        return "⚠️ À AMÉLIORER";
    }

    // 4. Top centres d'intérêt
    public List<Map<String, Object>> getTopInterests() {
        List<Object[]> topTags = membreTagRepository.findMostPopularTags();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] tag : topTags) {
            Map<String, Object> item = new HashMap<>();
            item.put("tag", tag[0]);
            item.put("count", tag[1]);
            item.put("icon", getTagIcon(tag[0].toString()));
            result.add(item);
            if (result.size() >= 10) break;
        }

        return result;
    }

    private String getTagIcon(String tag) {
        switch (tag.toUpperCase()) {
            case "ROBOTIQUE": return "🤖";
            case "IA": return "🧠";
            case "SPORT": return "⚽";
            case "MUSIQUE": return "🎵";
            case "ART": return "🎨";
            case "CULTURE": return "📚";
            default: return "🏷️";
        }
    }
}