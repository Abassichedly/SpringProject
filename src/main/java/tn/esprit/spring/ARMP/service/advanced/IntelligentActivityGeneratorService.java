package tn.esprit.spring.ARMP.service.advanced;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.dto.ActivitySuggestionDTO;
import tn.esprit.spring.ARMP.entity.*;
import tn.esprit.spring.ARMP.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IntelligentActivityGeneratorService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ActiviteRepository activiteRepository;
    private final ParticipationRepository participationRepository;
    private final MembreTagRepository membreTagRepository;

    private static final Map<String, List<String>> CONTEXT_KEYWORDS = new HashMap<>();
    private static final Map<String, List<String>> ACTIVITY_TEMPLATES = new HashMap<>();

    static {
        CONTEXT_KEYWORDS.put("SCIENTIFIQUE", List.of("robotique", "IA", "algorithme", "innovation", "technologie"));
        CONTEXT_KEYWORDS.put("CULTUREL", List.of("art", "musique", "théâtre", "littérature", "cinéma"));
        CONTEXT_KEYWORDS.put("SPORTIF", List.of("football", "basket", "compétition", "entraînement", "tournoi"));
        CONTEXT_KEYWORDS.put("ARTISTIQUE", List.of("peinture", "dessin", "photographie", "sculpture", "créativité"));

        ACTIVITY_TEMPLATES.put("ATELIER", List.of(
                "Atelier {theme} : {description}",
                "{theme} Hands-on : Apprenez en pratiquant"
        ));
        ACTIVITY_TEMPLATES.put("CONFERENCE", List.of(
                "Conférence : {theme} - {description}",
                "Talk inspirant sur {theme}"
        ));
        ACTIVITY_TEMPLATES.put("FORMATION", List.of(
                "Formation accélérée : {theme}",
                "{theme} pour débutants : Devenez expert"
        ));
    }

    public List<ActivitySuggestionDTO> generateIntelligentActivities(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) return new ArrayList<>();

        List<ActivitySuggestionDTO> suggestions = new ArrayList<>();

        String domaine = club.getDomaine().name();
        List<String> keywords = CONTEXT_KEYWORDS.getOrDefault(domaine, List.of("club", "activité"));

        List<User> users = userRepository.findByClubId(clubId);
        List<String> userInterests = users.stream()
                .flatMap(u -> membreTagRepository.findTagsByUserId(u.getId()).stream())
                .map(MembreTag::getTag)
                .distinct()
                .collect(Collectors.toList());

        List<Activite> pastActivities = activiteRepository.findByClubId(clubId);
        Map<String, Integer> typePopularity = new HashMap<>();
        for (Activite a : pastActivities) {
            typePopularity.put(a.getType().name(), typePopularity.getOrDefault(a.getType().name(), 0) + 1);
        }

        List<String> allTypes = Arrays.asList("ATELIER", "CONFERENCE", "FORMATION", "REUNION");
        List<String> missingTypes = allTypes.stream()
                .filter(t -> !typePopularity.containsKey(t))
                .collect(Collectors.toList());

        for (String interest : userInterests) {
            ActivitySuggestionDTO suggestion = generateActivityFromInterest(interest, domaine, club);
            if (suggestion != null && suggestion.getScoreConfiance() > 0.4) {
                suggestions.add(suggestion);
            }
        }

        for (String missingType : missingTypes) {
            ActivitySuggestionDTO suggestion = generateActivityFromType(missingType, domaine, keywords, club);
            if (suggestion != null) {
                suggestions.add(suggestion);
            }
        }

        if (!pastActivities.isEmpty()) {
            ActivitySuggestionDTO trendySuggestion = generateTrendyActivity(club, pastActivities);
            if (trendySuggestion != null) {
                suggestions.add(trendySuggestion);
            }
        }

        suggestions.sort((a, b) -> Double.compare(b.getScoreConfiance(), a.getScoreConfiance()));

        return suggestions.stream().limit(10).collect(Collectors.toList());
    }

    private ActivitySuggestionDTO generateActivityFromInterest(String interest, String domaine, Club club) {
        String type = "ATELIER";
        String titre = "Découvrez " + interest + " : Atelier au " + club.getNom();
        String description = "Venez explorer le monde fascinant de " + interest + " à travers cet atelier interactif.";

        double confidence = 0.5 + (Math.random() * 0.3);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(type)
                .description(description)
                .lieuSuggere("Salle " + club.getNom())
                .heureSuggeree("15:00")
                .dureeEstimee(120)
                .participantsEstimes(10 + (int)(Math.random() * 30))
                .scoreConfiance(confidence)
                .tags(List.of(interest, domaine, type))
                .motsCles(generateKeywords(interest))
                .raison(generateReason(interest, club))
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    private ActivitySuggestionDTO generateActivityFromType(String type, String domaine, List<String> keywords, Club club) {
        String theme = keywords.get(new Random().nextInt(keywords.size()));
        List<String> templates = ACTIVITY_TEMPLATES.getOrDefault(type, List.of("Activité {theme} pour {club}"));
        String template = templates.get(new Random().nextInt(templates.size()));

        String titre = template.replace("{theme}", theme).replace("{club}", club.getNom());
        double confidence = 0.5 + (Math.random() * 0.3);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(type)
                .description("Une opportunité unique de découvrir " + theme)
                .lieuSuggere("Salle " + club.getNom())
                .heureSuggeree("16:00")
                .dureeEstimee(120)
                .participantsEstimes(10 + (int)(Math.random() * 40))
                .scoreConfiance(confidence)
                .tags(List.of(theme, type, domaine))
                .motsCles(List.of(theme.toLowerCase(), "découverte"))
                .raison("Type d'activité jamais organisé auparavant")
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    private ActivitySuggestionDTO generateTrendyActivity(Club club, List<Activite> pastActivities) {
        if (pastActivities.isEmpty()) return null;

        Map<String, Long> typeCount = pastActivities.stream()
                .collect(Collectors.groupingBy(a -> a.getType().name(), Collectors.counting()));

        String popularType = typeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("ATELIER");

        String titre = popularType + " Premium : Édition Spéciale";
        double confidence = 0.8 + (Math.random() * 0.15);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(popularType)
                .description("Basé sur le succès de nos activités passées, une version améliorée !")
                .lieuSuggere("Grande salle")
                .heureSuggeree("18:00")
                .dureeEstimee(180)
                .participantsEstimes(20 + (int)(Math.random() * 50))
                .scoreConfiance(confidence)
                .tags(List.of(popularType, "tendance", "premium"))
                .motsCles(List.of(popularType.toLowerCase(), "spécial"))
                .raison("Suite au succès des activités similaires")
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    private List<String> generateKeywords(String interest) {
        return List.of(interest.toLowerCase(), "activité", "club", "découverte");
    }

    private String generateReason(String interest, Club club) {
        long usersWithInterest = club.getUsers().stream()
                .filter(u -> membreTagRepository.findTagsByUserId(u.getId()).stream()
                        .anyMatch(t -> t.getTag().equalsIgnoreCase(interest)))
                .count();
        return usersWithInterest + " membres sont intéressés par " + interest;
    }
}