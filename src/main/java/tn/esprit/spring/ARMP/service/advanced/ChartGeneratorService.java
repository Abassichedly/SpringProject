package tn.esprit.spring.ARMP.service.advanced;

import tn.esprit.spring.ARMP.entity.Activite;
import tn.esprit.spring.ARMP.entity.Club;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.entity.MembreTag;
import tn.esprit.spring.ARMP.repository.ActiviteRepository;
import tn.esprit.spring.ARMP.repository.ClubRepository;
import tn.esprit.spring.ARMP.repository.MembreTagRepository;
import tn.esprit.spring.ARMP.repository.ParticipationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.dto.ChartDataDTO;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChartGeneratorService {

    private final ClubRepository clubRepository;
    private final MembreTagRepository membreTagRepository;
    private final ActiviteRepository activiteRepository;
    private final ParticipationRepository participationRepository;

    // 1. GRAPHIQUE D'ÉVOLUTION DES MEMBRES (LINE CHART)
    public ChartDataDTO generateMemberEvolutionChart(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) return null;

        List<User> users = club.getUsers();

        // Regrouper par mois d'adhésion
        Map<String, Long> monthlyData = users.stream()
                .filter(u -> u.getDateAdhesion() != null)
                .collect(Collectors.groupingBy(
                        u -> u.getDateAdhesion().format(DateTimeFormatter.ofPattern("MMM yyyy")),
                        Collectors.counting()
                ));

        List<String> labels = new ArrayList<>(monthlyData.keySet());
        Collections.sort(labels);

        List<Double> data = labels.stream()
                .map(monthlyData::get)
                .map(Long::doubleValue)
                .collect(Collectors.toList());

        return ChartDataDTO.builder()
                .type("LINE")
                .title("Évolution des adhésions - " + club.getNom())
                .labels(labels)
                .datasets(List.of(
                        ChartDataDTO.DatasetDTO.builder()
                                .label("Nouveaux membres")
                                .data(data)
                                .backgroundColor("rgba(54, 162, 235, 0.2)")
                                .borderColor("rgba(54, 162, 235, 1)")
                                .borderWidth(2.0)
                                .fill("origin")
                                .build()
                ))
                .options(Map.of("responsive", true, "maintainAspectRatio", false))
                .build();
    }

    // 2. GRAPHIQUE RADAR DES COMPÉTENCES DU CLUB
    public ChartDataDTO generateSkillsRadarChart(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) return null;

        List<User> users = club.getUsers();

        // Compter les tags par catégorie
        Map<String, Integer> skillScores = new HashMap<>();
        skillScores.put("Robotique", 0);
        skillScores.put("Programmation", 0);
        skillScores.put("Design", 0);
        skillScores.put("Communication", 0);
        skillScores.put("Organisation", 0);
        skillScores.put("Leadership", 0);

        for (User u : users) {
            // CORRIGÉ: findTagsByUserId au lieu de findTagsByMembreId
            List<MembreTag> tags = membreTagRepository.findTagsByUserId(u.getId());
            for (MembreTag tag : tags) {
                String tagName = tag.getTag().toUpperCase();
                if (tagName.contains("ROBOTIQUE")) skillScores.merge("Robotique", tag.getPoids(), Integer::sum);
                if (tagName.contains("CODE") || tagName.contains("PROG")) skillScores.merge("Programmation", tag.getPoids(), Integer::sum);
                if (tagName.contains("DESIGN") || tagName.contains("ART")) skillScores.merge("Design", tag.getPoids(), Integer::sum);
                if (tagName.contains("COMM")) skillScores.merge("Communication", tag.getPoids(), Integer::sum);
                if (tagName.contains("ORG")) skillScores.merge("Organisation", tag.getPoids(), Integer::sum);
                if (tagName.contains("LEAD") || tagName.contains("PRES")) skillScores.merge("Leadership", tag.getPoids(), Integer::sum);
            }
        }

        List<String> labels = new ArrayList<>(skillScores.keySet());
        List<Double> data = labels.stream()
                .map(skillScores::get)
                .map(Integer::doubleValue)
                .collect(Collectors.toList());

        return ChartDataDTO.builder()
                .type("RADAR")
                .title("Radar des compétences - " + club.getNom())
                .labels(labels)
                .datasets(List.of(
                        ChartDataDTO.DatasetDTO.builder()
                                .label("Niveau de compétence")
                                .data(data)
                                .backgroundColor("rgba(255, 99, 132, 0.2)")
                                .borderColor("rgba(255, 99, 132, 1)")
                                .borderWidth(2.0)
                                .fill("origin")
                                .build()
                ))
                .options(Map.of(
                        "responsive", true,
                        "maintainAspectRatio", false,
                        "scales", Map.of("r", Map.of("beginAtZero", true))
                ))
                .build();
    }

    // 3. HEATMAP DES ACTIVITÉS
    public ChartDataDTO generateActivityHeatmapChart(Long clubId) {
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
            String dayName = switch (day) {
                case 2 -> "LUNDI";
                case 3 -> "MARDI";
                case 4 -> "MERCREDI";
                case 5 -> "JEUDI";
                case 6 -> "VENDREDI";
                case 7 -> "SAMEDI";
                default -> "DIMANCHE";
            };
            dayMap.put(dayName, count);
        }

        List<String> labels = new ArrayList<>(dayMap.keySet());
        List<Double> data = labels.stream()
                .map(dayMap::get)
                .map(Long::doubleValue)
                .collect(Collectors.toList());

        return ChartDataDTO.builder()
                .type("BAR")
                .title("Heatmap des activités par jour")
                .labels(labels)
                .datasets(List.of(
                        ChartDataDTO.DatasetDTO.builder()
                                .label("Nombre d'activités")
                                .data(data)
                                .backgroundColor("rgba(255, 159, 64, 0.5)")
                                .borderColor("rgba(255, 159, 64, 1)")
                                .borderWidth(1.0)
                                .build()
                ))
                .options(Map.of(
                        "responsive", true,
                        "maintainAspectRatio", false,
                        "scales", Map.of(
                                "y", Map.of("beginAtZero", true, "title", Map.of("display", true, "text", "Nombre d'activités")),
                                "x", Map.of("title", Map.of("display", true, "text", "Jour de la semaine"))
                        )
                ))
                .build();
    }

    // 4. GRAPHIQUE CIRCULAIRE DES TYPES D'ACTIVITÉS
    public ChartDataDTO generateActivityTypesPieChart(Long clubId) {
        List<Activite> activites = activiteRepository.findByClubIdClub(clubId);

        Map<String, Long> typeCount = activites.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getType().name(),
                        Collectors.counting()
                ));

        List<String> labels = new ArrayList<>(typeCount.keySet());
        List<Double> data = labels.stream()
                .map(typeCount::get)
                .map(Long::doubleValue)
                .collect(Collectors.toList());

        List<String> colors = List.of(
                "rgba(255, 99, 132, 0.7)",
                "rgba(54, 162, 235, 0.7)",
                "rgba(255, 206, 86, 0.7)",
                "rgba(75, 192, 192, 0.7)"
        );

        return ChartDataDTO.builder()
                .type("PIE")
                .title("Répartition des types d'activités")
                .labels(labels)
                .datasets(List.of(
                        ChartDataDTO.DatasetDTO.builder()
                                .label("Types")
                                .data(data)
                                .backgroundColor(String.join(",", colors))
                                .borderWidth(1.0)
                                .build()
                ))
                .options(Map.of("responsive", true, "maintainAspectRatio", false))
                .build();
    }

    // 5. GRAPHIQUE COMPLET POUR DASHBOARD
    public Map<String, ChartDataDTO> generateFullDashboardCharts(Long clubId) {
        Map<String, ChartDataDTO> charts = new HashMap<>();
        charts.put("evolution", generateMemberEvolutionChart(clubId));
        charts.put("skills", generateSkillsRadarChart(clubId));
        charts.put("heatmap", generateActivityHeatmapChart(clubId));
        charts.put("activityTypes", generateActivityTypesPieChart(clubId));
        return charts;
    }
}