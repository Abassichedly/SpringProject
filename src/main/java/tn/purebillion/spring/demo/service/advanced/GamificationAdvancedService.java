package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.MembreTag;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.repository.MembreTagRepository;
import tn.purebillion.spring.demo.repository.ParticipationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GamificationAdvancedService {

    private final MembreRepository membreRepository;
    private final ParticipationRepository participationRepository;
    private final MembreTagRepository membreTagRepository;

    private int calculateXP(Long membreId) {
        List<Participation> participations = participationRepository.findByMembreIdMembre(membreId);
        int xp = participations.size() * 10;

        long presences = participations.stream()
                .filter(p -> p.getStatutPresence() != null &&
                        p.getStatutPresence().name().equals("PRESENT"))
                .count();
        xp += (int) presences * 5;

        // CORRIGÉ : Utiliser findByMembreId
        int tagsCount = membreTagRepository.findByMembreId(membreId).size();
        xp += tagsCount * 2;

        return xp;
    }

    private List<String> getBadges(Long membreId) {
        List<String> badges = new ArrayList<>();
        int xp = calculateXP(membreId);

        if (xp >= 1000) badges.add("🏆 LÉGENDE");
        else if (xp >= 500) badges.add("🏅 MASTER");
        else if (xp >= 200) badges.add("⭐ EXPERT");
        else if (xp >= 50) badges.add("🌟 APPRENTI");

        long participations = participationRepository.findByMembreIdMembre(membreId).size();
        if (participations >= 50) badges.add("🎯 FIDÈLE");
        if (participations >= 100) badges.add("💎 ULTRA-FIDÈLE");

        // CORRIGÉ : Utiliser findByMembreId
        int tagsCount = membreTagRepository.findByMembreId(membreId).size();
        if (tagsCount >= 5) badges.add("🏷️ POLYVALENT");

        return badges;
    }

    public List<Map<String, Object>> generateFullLeaderboard() {
        List<Membre> allMembres = membreRepository.findAll();
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (Membre m : allMembres) {
            int xp = calculateXP(m.getIdMembre());

            Map<String, Object> entry = new HashMap<>();
            entry.put("id", m.getIdMembre());
            entry.put("membre", m.getNom() + " " + m.getPrenom());
            entry.put("xp", xp);
            entry.put("level", xp / 100 + 1);
            entry.put("badges", getBadges(m.getIdMembre()));
            entry.put("title", getTitle(xp));
            entry.put("club", m.getClub() != null ? m.getClub().getNom() : "Aucun");
            leaderboard.add(entry);
        }

        leaderboard.sort((a, b) -> Integer.compare((int) b.get("xp"), (int) a.get("xp")));

        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).put("rank", i + 1);
        }

        return leaderboard;
    }

    private String getTitle(int xp) {
        if (xp >= 1000) return "Grand Maître";
        if (xp >= 500) return "Expert Confirmé";
        if (xp >= 200) return "Membre Actif";
        if (xp >= 50) return "Nouveau Talent";
        return "Débutant";
    }

    public Map<String, Object> getPersonalizedJourney(Long membreId) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Membre non trouvé");
            return error;
        }

        int xp = calculateXP(membreId);
        int currentLevel = xp / 100 + 1;
        int nextLevelXp = currentLevel * 100;
        int xpNeeded = nextLevelXp - xp;

        List<String> nextBadges = new ArrayList<>();
        if (xp < 50) nextBadges.add("🌟 APPRENTI (50 XP)");
        if (xp < 200) nextBadges.add("⭐ EXPERT (200 XP)");
        if (xp < 500) nextBadges.add("🏅 MASTER (500 XP)");
        if (xp < 1000) nextBadges.add("🏆 LÉGENDE (1000 XP)");

        List<String> suggestedActivities = new ArrayList<>();
        suggestedActivities.add("Participer à 3 activités → +30 XP");
        suggestedActivities.add("Laisser un feedback → +20 XP");
        suggestedActivities.add("Ajouter des centres d'intérêt → +10 XP par tag");

        if (xpNeeded > 0) {
            suggestedActivities.add("Encore " + xpNeeded + " XP pour passer niveau " + (currentLevel + 1));
        }

        // CORRIGÉ : Utiliser findByMembreId
        List<String> tags = membreTagRepository.findByMembreId(membreId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("membre", membre.getNom() + " " + membre.getPrenom());
        result.put("currentLevel", currentLevel);
        result.put("currentXP", xp);
        result.put("xpToNextLevel", xpNeeded);
        result.put("nextBadges", nextBadges);
        result.put("suggestedActivities", suggestedActivities);
        result.put("badges", getBadges(membreId));
        result.put("centresInteret", tags);

        return result;
    }

    public Map<String, Object> getMemberStats(Long membreId) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Membre non trouvé");
            return error;
        }

        int xp = calculateXP(membreId);
        List<Participation> participationsList = participationRepository.findByMembreIdMembre(membreId);
        long participations = participationsList.size();
        long presences = participationsList.stream()
                .filter(p -> p.getStatutPresence() != null && p.getStatutPresence().name().equals("PRESENT"))
                .count();

        // CORRIGÉ : Utiliser findByMembreId
        List<String> tags = membreTagRepository.findByMembreId(membreId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("membre", membre.getNom() + " " + membre.getPrenom());
        result.put("xp", xp);
        result.put("level", xp / 100 + 1);
        result.put("totalParticipations", participations);
        result.put("presences", presences);
        result.put("tauxPresence", participations > 0 ? (int)(presences * 100 / participations) : 0);
        result.put("centresInteret", tags);
        result.put("badges", getBadges(membreId));

        return result;
    }
}