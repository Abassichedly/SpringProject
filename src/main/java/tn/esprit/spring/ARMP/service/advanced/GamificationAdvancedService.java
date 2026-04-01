package tn.esprit.spring.ARMP.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.entity.MembreTag;
import tn.esprit.spring.ARMP.entity.Participation;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.repository.MembreTagRepository;
import tn.esprit.spring.ARMP.repository.ParticipationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GamificationAdvancedService {

    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final MembreTagRepository membreTagRepository;

    private int calculateXP(String userId) {
        List<Participation> participations = participationRepository.findByUserId(userId);
        int xp = participations.size() * 10;

        long presences = participations.stream()
                .filter(p -> p.getStatutPresence() != null &&
                        p.getStatutPresence().name().equals("PRESENT"))
                .count();
        xp += (int) presences * 5;

        int tagsCount = membreTagRepository.findByUserId(userId).size();
        xp += tagsCount * 2;

        return xp;
    }

    private List<String> getBadges(String userId) {
        List<String> badges = new ArrayList<>();
        int xp = calculateXP(userId);

        if (xp >= 1000) badges.add("🏆 LÉGENDE");
        else if (xp >= 500) badges.add("🏅 MASTER");
        else if (xp >= 200) badges.add("⭐ EXPERT");
        else if (xp >= 50) badges.add("🌟 APPRENTI");

        long participations = participationRepository.findByUserId(userId).size();
        if (participations >= 50) badges.add("🎯 FIDÈLE");
        if (participations >= 100) badges.add("💎 ULTRA-FIDÈLE");

        int tagsCount = membreTagRepository.findByUserId(userId).size();
        if (tagsCount >= 5) badges.add("🏷️ POLYVALENT");

        return badges;
    }

    public List<Map<String, Object>> generateFullLeaderboard() {
        List<User> allUsers = userRepository.findAll();
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (User u : allUsers) {
            int xp = calculateXP(u.getId());

            Map<String, Object> entry = new HashMap<>();
            entry.put("id", u.getId());
            entry.put("user", u.getFirstName() + " " + u.getLastName());
            entry.put("xp", xp);
            entry.put("level", xp / 100 + 1);
            entry.put("badges", getBadges(u.getId()));
            entry.put("title", getTitle(xp));
            entry.put("club", u.getClub() != null ? u.getClub().getNom() : "Aucun");
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

    public Map<String, Object> getPersonalizedJourney(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Utilisateur non trouvé");
            return error;
        }

        int xp = calculateXP(userId);
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

        List<String> tags = membreTagRepository.findByUserId(userId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("user", user.getFirstName() + " " + user.getLastName());
        result.put("currentLevel", currentLevel);
        result.put("currentXP", xp);
        result.put("xpToNextLevel", xpNeeded);
        result.put("nextBadges", nextBadges);
        result.put("suggestedActivities", suggestedActivities);
        result.put("badges", getBadges(userId));
        result.put("centresInteret", tags);

        return result;
    }

    public Map<String, Object> getUserStats(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Utilisateur non trouvé");
            return error;
        }

        int xp = calculateXP(userId);
        List<Participation> participationsList = participationRepository.findByUserId(userId);
        long participations = participationsList.size();
        long presences = participationsList.stream()
                .filter(p -> p.getStatutPresence() != null && p.getStatutPresence().name().equals("PRESENT"))
                .count();

        List<String> tags = membreTagRepository.findByUserId(userId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("user", user.getFirstName() + " " + user.getLastName());
        result.put("xp", xp);
        result.put("level", xp / 100 + 1);
        result.put("totalParticipations", participations);
        result.put("presences", presences);
        result.put("tauxPresence", participations > 0 ? (int)(presences * 100 / participations) : 0);
        result.put("centresInteret", tags);
        result.put("badges", getBadges(userId));

        return result;
    }
}