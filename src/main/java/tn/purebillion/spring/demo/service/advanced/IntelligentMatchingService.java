package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.dto.MentorMatchDTO;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.MembreTag;
import tn.purebillion.spring.demo.enums.RoleMembre;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.repository.MembreTagRepository;
import tn.purebillion.spring.demo.repository.ParticipationRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IntelligentMatchingService {

    private final MembreRepository membreRepository;
    private final MembreTagRepository membreTagRepository;
    private final ParticipationRepository participationRepository;

    // 1. TROUVER LE MEILLEUR MENTOR POUR UN MEMBRE
    public List<MentorMatchDTO> findBestMentors(Long membreId, int limit) {
        Membre mentore = membreRepository.findById(membreId).orElse(null);
        if (mentore == null) return new ArrayList<>();

        // Récupérer tous les mentors potentiels (Présidents, VPs, et membres expérimentés)
        List<Membre> mentors = new ArrayList<>();
        mentors.addAll(membreRepository.findByRole(RoleMembre.PRESIDENT));
        mentors.addAll(membreRepository.findByRole(RoleMembre.VICE_PRESIDENT));

        // Si pas assez de mentors, ajouter des membres avec beaucoup de participations
        if (mentors.size() < 3) {
            List<Object[]> topActive = participationRepository.findTopActiveMembres();
            for (Object[] obj : topActive) {
                Membre m = (Membre) obj[0];
                if (!m.getIdMembre().equals(membreId) && !mentors.contains(m)) {
                    mentors.add(m);
                    if (mentors.size() >= 5) break;
                }
            }
        }

        List<MentorMatchDTO> matches = new ArrayList<>();

        for (Membre mentor : mentors) {
            if (mentor.getIdMembre().equals(membreId)) continue;

            double matchScore = calculateMatchScore(mentor, mentore);
            if (matchScore > 0.2) {
                matches.add(createMatchDTO(mentor, mentore, matchScore));
            }
        }

        matches.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        return matches.stream().limit(limit).collect(Collectors.toList());
    }

    // 2. TROUVER LE MEILLEUR MENTORÉ POUR UN MENTOR
    public List<MentorMatchDTO> findBestMentees(Long mentorId, int limit) {
        Membre mentor = membreRepository.findById(mentorId).orElse(null);
        if (mentor == null) return new ArrayList<>();

        List<Membre> mentees = membreRepository.findByRole(RoleMembre.MEMBRE_SIMPLE);

        List<MentorMatchDTO> matches = new ArrayList<>();

        for (Membre mentee : mentees) {
            if (mentee.getIdMembre().equals(mentorId)) continue;

            double matchScore = calculateMatchScore(mentor, mentee);
            if (matchScore > 0.2) {
                matches.add(createMatchDTO(mentor, mentee, matchScore));
            }
        }

        matches.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        return matches.stream().limit(limit).collect(Collectors.toList());
    }

    // 3. ALGORITHME DE CALCUL DU SCORE DE MATCH
    private double calculateMatchScore(Membre mentor, Membre mentee) {
        Map<String, Double> scores = new HashMap<>();

        // Critère 1: Intérêts communs (40%)
        double interestScore = calculateInterestSimilarity(mentor, mentee);
        scores.put("Intérêts communs", interestScore * 0.4);

        // Critère 2: Compétences complémentaires (25%)
        double complementarityScore = calculateSkillComplementarity(mentor, mentee);
        scores.put("Compétences complémentaires", complementarityScore * 0.25);

        // Critère 3: Disponibilité (20%)
        double availabilityScore = calculateAvailabilityScore(mentor, mentee);
        scores.put("Disponibilité", availabilityScore * 0.2);

        // Critère 4: Expérience du mentor (15%)
        double experienceScore = calculateExperienceScore(mentor);
        scores.put("Expérience du mentor", experienceScore * 0.15);

        return scores.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // 4. SIMILARITÉ DES INTÉRÊTS
    private double calculateInterestSimilarity(Membre m1, Membre m2) {
        List<String> tags1 = membreTagRepository.findTagsByMembreId(m1.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());
        List<String> tags2 = membreTagRepository.findTagsByMembreId(m2.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        if (tags1.isEmpty() || tags2.isEmpty()) return 0.3;

        long common = tags1.stream().filter(tags2::contains).count();
        double similarity = (double) common / Math.max(tags1.size(), tags2.size());

        return Math.min(0.95, similarity + 0.2);
    }

    // 5. COMPLÉMENTARITÉ DES COMPÉTENCES
    private double calculateSkillComplementarity(Membre mentor, Membre mentee) {
        List<String> mentorSkills = membreTagRepository.findTagsByMembreId(mentor.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());
        List<String> menteeSkills = membreTagRepository.findTagsByMembreId(mentee.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        if (mentorSkills.isEmpty()) return 0.5;

        // Compétences que le mentee n'a pas mais que le mentor a
        long complementary = mentorSkills.stream()
                .filter(s -> !menteeSkills.contains(s))
                .count();

        return Math.min(0.9, 0.5 + (complementary * 0.05));
    }

    // 6. SCORE DE DISPONIBILITÉ
    private double calculateAvailabilityScore(Membre mentor, Membre mentee) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        long mentorParticipations = participationRepository.findByMembreIdMembre(mentor.getIdMembre())
                .stream().filter(p -> p.getDateInscription() != null && p.getDateInscription().isAfter(threeMonthsAgo))
                .count();

        long menteeParticipations = participationRepository.findByMembreIdMembre(mentee.getIdMembre())
                .stream().filter(p -> p.getDateInscription() != null && p.getDateInscription().isAfter(threeMonthsAgo))
                .count();

        // Moins de participations = plus de disponibilité
        double mentorAvailability = Math.max(0.2, 1 - (mentorParticipations / 30.0));
        double menteeAvailability = Math.max(0.2, 1 - (menteeParticipations / 30.0));

        return (mentorAvailability + menteeAvailability) / 2;
    }

    // 7. SCORE D'EXPÉRIENCE (CORRIGÉ - GESTION DES NULL)
    private double calculateExperienceScore(Membre mentor) {
        // Score basé sur le nombre de participations
        long totalParticipations = participationRepository.findByMembreIdMembre(mentor.getIdMembre()).size();
        double participationScore = Math.min(0.5, totalParticipations / 40.0);

        // Score basé sur l'ancienneté (si dateAdhesion non null)
        double ancienneteScore = 0;
        if (mentor.getDateAdhesion() != null) {
            long ancienneteJours = java.time.temporal.ChronoUnit.DAYS.between(
                    mentor.getDateAdhesion(), LocalDate.now());
            ancienneteScore = Math.min(0.5, ancienneteJours / 730.0); // 2 ans max
        } else {
            ancienneteScore = 0.2; // Valeur par défaut
        }

        // Score basé sur le rôle
        double roleScore = 0;
        if (mentor.getRole() == RoleMembre.PRESIDENT) roleScore = 0.3;
        else if (mentor.getRole() == RoleMembre.VICE_PRESIDENT) roleScore = 0.2;
        else if (mentor.getRole() == RoleMembre.SECRETAIRE) roleScore = 0.15;
        else roleScore = 0.05;

        return Math.min(0.95, participationScore + ancienneteScore + roleScore);
    }

    // 8. CRÉER LE DTO DE MATCH
    private MentorMatchDTO createMatchDTO(Membre mentor, Membre mentee, double score) {
        List<String> commonInterests = findCommonInterests(mentor, mentee);
        List<String> complementarySkills = findComplementarySkills(mentor, mentee);

        String matchLevel;
        if (score >= 0.8) matchLevel = "EXCEPTIONNEL 🔥";
        else if (score >= 0.7) matchLevel = "EXCELLENT ⭐";
        else if (score >= 0.6) matchLevel = "TRÈS BON 👍";
        else if (score >= 0.5) matchLevel = "BON 👌";
        else matchLevel = "MOYEN 📌";

        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("Intérêts communs", calculateInterestSimilarity(mentor, mentee));
        breakdown.put("Compétences complémentaires", calculateSkillComplementarity(mentor, mentee));
        breakdown.put("Disponibilité", calculateAvailabilityScore(mentor, mentee));
        breakdown.put("Expérience", calculateExperienceScore(mentor));

        List<String> recommendations = generateRecommendations(score, commonInterests, complementarySkills);

        return MentorMatchDTO.builder()
                .mentor(mentor)
                .mentore(mentee)
                .matchScore(score)
                .matchLevel(matchLevel)
                .commonInterests(commonInterests)
                .complementarySkills(complementarySkills)
                .scoreBreakdown(breakdown)
                .recommendations(recommendations)
                .build();
    }

    // 9. TROUVER LES INTÉRÊTS COMMUNS
    private List<String> findCommonInterests(Membre m1, Membre m2) {
        List<String> tags1 = membreTagRepository.findTagsByMembreId(m1.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());
        List<String> tags2 = membreTagRepository.findTagsByMembreId(m2.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        return tags1.stream().filter(tags2::contains).collect(Collectors.toList());
    }

    // 10. TROUVER LES COMPÉTENCES COMPLÉMENTAIRES
    private List<String> findComplementarySkills(Membre mentor, Membre mentee) {
        List<String> mentorSkills = membreTagRepository.findTagsByMembreId(mentor.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());
        List<String> menteeSkills = membreTagRepository.findTagsByMembreId(mentee.getIdMembre())
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        return mentorSkills.stream()
                .filter(s -> !menteeSkills.contains(s))
                .limit(5)
                .collect(Collectors.toList());
    }

    // 11. GÉNÉRER DES RECOMMANDATIONS
    private List<String> generateRecommendations(double score, List<String> commonInterests, List<String> complementarySkills) {
        List<String> recommendations = new ArrayList<>();

        if (score >= 0.7) {
            recommendations.add("🔥 Match exceptionnel ! Une collaboration prometteuse");
            recommendations.add("Organisez une première rencontre cette semaine");
        } else if (score >= 0.5) {
            recommendations.add("👍 Bon potentiel de collaboration");
            if (!commonInterests.isEmpty()) {
                recommendations.add("Commencez par un projet commun sur " + commonInterests.get(0));
            }
        } else {
            recommendations.add("📌 Match modéré, mais peut être intéressant");
            recommendations.add("Essayez une activité commune pour tester la dynamique");
        }

        if (!complementarySkills.isEmpty()) {
            recommendations.add("💡 Le mentor peut transmettre: " + String.join(", ", complementarySkills.subList(0, Math.min(3, complementarySkills.size()))));
        }

        if (!commonInterests.isEmpty()) {
            recommendations.add("🎯 Points communs: " + String.join(", ", commonInterests.subList(0, Math.min(3, commonInterests.size()))));
        }

        return recommendations;
    }
}