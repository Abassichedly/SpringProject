package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.dto.ActivitySuggestionDTO;
import tn.purebillion.spring.demo.entity.*;
import tn.purebillion.spring.demo.enums.TypeActivite;
import tn.purebillion.spring.demo.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IntelligentActivityGeneratorService {

    private final ClubRepository clubRepository;
    private final MembreRepository membreRepository;
    private final ActiviteRepository activiteRepository;
    private final ParticipationRepository participationRepository;
    private final MembreTagRepository membreTagRepository;

    // Dictionnaire de mots-clés pour la génération contextuelle
    private static final Map<String, List<String>> CONTEXT_KEYWORDS = new HashMap<>();
    private static final Map<String, List<String>> ACTIVITY_TEMPLATES = new HashMap<>();

    static {
        // Mots-clés par domaine
        CONTEXT_KEYWORDS.put("SCIENTIFIQUE", List.of("robotique", "IA", "algorithme", "innovation", "technologie"));
        CONTEXT_KEYWORDS.put("CULTUREL", List.of("art", "musique", "théâtre", "littérature", "cinéma"));
        CONTEXT_KEYWORDS.put("SPORTIF", List.of("football", "basket", "compétition", "entraînement", "tournoi"));
        CONTEXT_KEYWORDS.put("ARTISTIQUE", List.of("peinture", "dessin", "photographie", "sculpture", "créativité"));

        // Templates d'activités
        ACTIVITY_TEMPLATES.put("ATELIER", List.of(
                "Atelier {theme} : {description}",
                "{theme} Hands-on : Apprenez en pratiquant",
                "Masterclass {theme} avec des experts"
        ));
        ACTIVITY_TEMPLATES.put("CONFERENCE", List.of(
                "Conférence : {theme} - {description}",
                "Talk inspirant sur {theme}",
                "Rencontre avec des professionnels du {theme}"
        ));
        ACTIVITY_TEMPLATES.put("FORMATION", List.of(
                "Formation accélérée : {theme}",
                "{theme} pour débutants : Devenez expert",
                "Certification {theme} : Validez vos compétences"
        ));
    }

    // 1. GÉNÉRER DES ACTIVITÉS INTELLIGENTES
    public List<ActivitySuggestionDTO> generateIntelligentActivities(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) return new ArrayList<>();

        List<ActivitySuggestionDTO> suggestions = new ArrayList<>();

        // Analyser le club
        String domaine = club.getDomaine().name();
        List<String> keywords = CONTEXT_KEYWORDS.getOrDefault(domaine, List.of("club", "activité"));

        // Analyser les membres et leurs intérêts
        List<Membre> membres = membreRepository.findByClubIdClub(clubId);
        List<String> memberInterests = membres.stream()
                .flatMap(m -> membreTagRepository.findTagsByMembreId(m.getIdMembre()).stream())
                .map(t -> t.getTag())
                .distinct()
                .collect(Collectors.toList());

        // Analyser les activités passées
        List<Activite> pastActivities = activiteRepository.findByClubIdClub(clubId);
        Map<String, Integer> typePopularity = new HashMap<>();
        for (Activite a : pastActivities) {
            typePopularity.put(a.getType().name(), typePopularity.getOrDefault(a.getType().name(), 0) + 1);
        }

        // Trouver les types sous-représentés
        List<String> allTypes = Arrays.asList("ATELIER", "CONFERENCE", "FORMATION", "REUNION");
        List<String> missingTypes = allTypes.stream()
                .filter(t -> !typePopularity.containsKey(t))
                .collect(Collectors.toList());

        // Générer des suggestions basées sur les intérêts des membres
        for (String interest : memberInterests) {
            ActivitySuggestionDTO suggestion = generateActivityFromInterest(interest, domaine, club);
            if (suggestion != null) {
                suggestions.add(suggestion);
            }
        }

        // Générer des suggestions pour les types manquants
        for (String missingType : missingTypes) {
            ActivitySuggestionDTO suggestion = generateActivityFromType(missingType, domaine, keywords, club);
            if (suggestion != null) {
                suggestions.add(suggestion);
            }
        }

        // Générer des suggestions basées sur les tendances
        ActivitySuggestionDTO trendySuggestion = generateTrendyActivity(club, pastActivities);
        if (trendySuggestion != null) {
            suggestions.add(trendySuggestion);
        }

        // Trier par score de confiance
        suggestions.sort((a, b) -> Double.compare(b.getScoreConfiance(), a.getScoreConfiance()));

        return suggestions.stream().limit(10).collect(Collectors.toList());
    }

    // 2. GÉNÉRER UNE ACTIVITÉ À PARTIR D'UN CENTRE D'INTÉRÊT
    private ActivitySuggestionDTO generateActivityFromInterest(String interest, String domaine, Club club) {
        // Choisir le type d'activité le plus adapté
        String type = selectOptimalType(interest);

        // Générer un titre créatif
        String titre = generateCreativeTitle(interest, type, club.getNom());

        // Générer une description
        String description = generateDescription(interest, type, domaine);

        // Suggérer un lieu
        String lieu = suggestLocation(interest, club);

        // Suggérer un horaire
        String horaire = suggestTime();

        // Calculer le score de confiance
        double confidence = calculateConfidenceScore(interest, club);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(type)
                .description(description)
                .lieuSuggere(lieu)
                .heureSuggeree(horaire)
                .dureeEstimee(generateDuration())
                .participantsEstimes(estimateParticipants(interest, club))
                .scoreConfiance(confidence)
                .tags(List.of(interest, domaine, type))
                .motsCles(generateKeywords(interest))
                .raison(generateReason(interest, club))
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    // 3. GÉNÉRER UNE ACTIVITÉ À PARTIR D'UN TYPE
    private ActivitySuggestionDTO generateActivityFromType(String type, String domaine, List<String> keywords, Club club) {
        String theme = selectTheme(keywords, domaine);

        List<String> templates = ACTIVITY_TEMPLATES.getOrDefault(type, List.of("Activité {theme} pour {club}"));
        String template = templates.get(new Random().nextInt(templates.size()));

        String titre = template
                .replace("{theme}", theme)
                .replace("{club}", club.getNom());

        String description = "Une opportunité unique de découvrir " + theme +
                " dans le cadre du " + club.getNom() +
                ". " + generateDescriptionComplement(theme);

        double confidence = 0.5 + (Math.random() * 0.3);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(type)
                .description(description)
                .lieuSuggere(suggestLocation(theme, club))
                .heureSuggeree(suggestTime())
                .dureeEstimee(120 + (int)(Math.random() * 120))
                .participantsEstimes(10 + (int)(Math.random() * 40))
                .scoreConfiance(confidence)
                .tags(List.of(theme, type, domaine))
                .motsCles(List.of(theme.toLowerCase(), "découverte", "apprentissage"))
                .raison("Type d'activité jamais organisé auparavant")
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    // 4. GÉNÉRER UNE ACTIVITÉ TENDANCE
    private ActivitySuggestionDTO generateTrendyActivity(Club club, List<Activite> pastActivities) {
        if (pastActivities.isEmpty()) return null;

        // Trouver le type le plus populaire
        Map<String, Long> typeCount = pastActivities.stream()
                .collect(Collectors.groupingBy(a -> a.getType().name(), Collectors.counting()));

        String popularType = typeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("ATELIER");

        // Analyser les heures populaires
        Map<Integer, Long> hourPopularity = pastActivities.stream()
                .filter(a -> a.getHeureDebut() != null)
                .collect(Collectors.groupingBy(a -> a.getHeureDebut().getHour(), Collectors.counting()));

        int bestHour = hourPopularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(18);

        // Créer une variante améliorée
        String titre = popularType + " Premium : Édition Spéciale";
        String description = "Basé sur le succès de nos activités passées, nous vous proposons une version améliorée";
        double confidence = 0.8 + (Math.random() * 0.15);

        return ActivitySuggestionDTO.builder()
                .titre(titre)
                .type(popularType)
                .description(description)
                .lieuSuggere(suggestLocation(popularType, club))
                .heureSuggeree(bestHour + ":00")
                .dureeEstimee(180)
                .participantsEstimes(estimateParticipants(popularType, club))
                .scoreConfiance(confidence)
                .tags(List.of(popularType, "tendance", "premium"))
                .motsCles(List.of(popularType.toLowerCase(), "spécial", "édition limitée"))
                .raison("Suite au succès des activités similaires")
                .dateSuggestion(LocalDateTime.now())
                .build();
    }

    // 5. FONCTIONS UTILITAIRES INTELLIGENTES

    private String selectOptimalType(String interest) {
        if (interest.contains("ROBOTIQUE") || interest.contains("IA") || interest.contains("CODE")) {
            return "ATELIER";
        } else if (interest.contains("CONFERENCE") || interest.contains("TALK")) {
            return "CONFERENCE";
        } else if (interest.contains("FORMATION") || interest.contains("APPRENDRE")) {
            return "FORMATION";
        }
        return "ATELIER";
    }

    private String generateCreativeTitle(String interest, String type, String clubName) {
        String[] prefixes = {"Découvrez", "Explorez", "Maîtrisez", "Plongez dans", "Initiez-vous à"};
        String[] suffixes = {"au " + clubName, "avec " + clubName, "chez " + clubName, ""};

        String prefix = prefixes[new Random().nextInt(prefixes.length)];
        String suffix = suffixes[new Random().nextInt(suffixes.length)];

        return prefix + " " + interest + " : " + type + " " + suffix;
    }

    private String generateDescription(String interest, String type, String domaine) {
        String[] descriptions = {
                "Venez explorer le monde fascinant de " + interest + " à travers cet atelier interactif.",
                "Une opportunité unique de développer vos compétences en " + interest + ".",
                "Rejoignez-nous pour une expérience immersive dans l'univers de " + interest + ".",
                "Découvrez les secrets de " + interest + " avec des experts passionnés."
        };
        return descriptions[new Random().nextInt(descriptions.length)] +
                " " + generateDescriptionComplement(interest);
    }

    private String generateDescriptionComplement(String interest) {
        String[] complements = {
                "Idéal pour les débutants comme pour les confirmés.",
                "Matériel fourni sur place.",
                "Aucun prérequis nécessaire.",
                "Venez avec votre curiosité et votre enthousiasme !"
        };
        return complements[new Random().nextInt(complements.length)];
    }

    private String suggestLocation(String theme, Club club) {
        String[] locations = {"Salle polyvalente", "Amphithéâtre", "Laboratoire", "Espace créatif", "Terrain extérieur"};
        String baseLocation = locations[new Random().nextInt(locations.length)];
        return baseLocation + " - " + club.getNom();
    }

    private String suggestTime() {
        String[] times = {"14:00", "15:00", "16:00", "17:00", "18:00", "19:00"};
        return times[new Random().nextInt(times.length)];
    }

    private Integer generateDuration() {
        Integer[] durations = {60, 90, 120, 150, 180, 240};
        return durations[new Random().nextInt(durations.length)];
    }

    private Integer estimateParticipants(String interest, Club club) {
        int nbMembres = club.getMembres().size();
        int base = nbMembres / 3;
        int random = (int)(Math.random() * nbMembres / 3);
        return Math.max(5, base + random);
    }

    private double calculateConfidenceScore(String interest, Club club) {
        // Plus l'intérêt est populaire, plus la confiance est élevée
        long membresAvecInteret = club.getMembres().stream()
                .filter(m -> membreTagRepository.findTagsByMembreId(m.getIdMembre()).stream()
                        .anyMatch(t -> t.getTag().equalsIgnoreCase(interest)))
                .count();

        double baseConfidence = (double) membresAvecInteret / club.getMembres().size();
        return Math.min(0.95, baseConfidence + 0.2);
    }

    private List<String> generateKeywords(String interest) {
        return List.of(interest.toLowerCase(), "activité", "club", "découverte", "apprentissage");
    }

    private String generateReason(String interest, Club club) {
        long membresAvecInteret = club.getMembres().stream()
                .filter(m -> membreTagRepository.findTagsByMembreId(m.getIdMembre()).stream()
                        .anyMatch(t -> t.getTag().equalsIgnoreCase(interest)))
                .count();

        return membresAvecInteret + " membres sont intéressés par " + interest + " selon leurs tags";
    }

    private String selectTheme(List<String> keywords, String domaine) {
        if (!keywords.isEmpty()) {
            return keywords.get(new Random().nextInt(keywords.size()));
        }
        return domaine;
    }
}