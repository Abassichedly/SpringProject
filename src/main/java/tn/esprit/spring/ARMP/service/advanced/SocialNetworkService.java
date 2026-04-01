package tn.esprit.spring.ARMP.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Interaction;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.entity.MembreTag;
import tn.esprit.spring.ARMP.repository.InteractionRepository;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.repository.MembreTagRepository;
import tn.esprit.spring.ARMP.repository.ParticipationRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SocialNetworkService {

    private final UserRepository userRepository;
    private final InteractionRepository interactionRepository;
    private final MembreTagRepository membreTagRepository;
    private final ParticipationRepository participationRepository; // AJOUTER

    public List<Map<String, Object>> recommendConnections(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        Map<User, Integer> scores = new HashMap<>();
        List<String> myTags = membreTagRepository.findTagsByUserId(userId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        for (User other : userRepository.findAll()) {
            if (other.getId().equals(userId)) continue;

            int score = 0;

            if (user.getClub() != null && user.getClub().equals(other.getClub())) {
                score += 50;
            }

            List<String> otherTags = membreTagRepository.findTagsByUserId(other.getId())
                    .stream().map(MembreTag::getTag).collect(Collectors.toList());
            long commonTags = myTags.stream().filter(otherTags::contains).count();
            score += (int) commonTags * 15;

            if (score > 0) {
                scores.put(other, score);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<User, Integer> entry : scores.entrySet().stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList())) {
            Map<String, Object> item = new HashMap<>();
            item.put("user", entry.getKey().getFirstName() + " " + entry.getKey().getLastName());
            item.put("id", entry.getKey().getId());
            item.put("score", entry.getValue());
            item.put("club", entry.getKey().getClub() != null ? entry.getKey().getClub().getNom() : "Aucun");
            result.add(item);
        }

        return result;
    }

    public Map<String, Object> calculateInfluenceScore(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Utilisateur non trouvé");
            return error;
        }

        int interactionsRecues = interactionRepository.countByUserCibleId(userId);
        int interactionsEnvoyees = interactionRepository.countByUserSourceId(userId);

        // CORRIGÉ: Utiliser le repository pour compter les participations
        int participations = participationRepository.findByUserId(userId).size();

        int tagsCount = membreTagRepository.findTagsByUserId(userId).size();

        double influenceScore = (interactionsRecues * 0.5) + (participations * 0.3) +
                (interactionsEnvoyees * 0.1) + (tagsCount * 0.1);

        String niveau;
        String badge;
        if (influenceScore > 100) {
            niveau = "INFLUENCEUR MAJEUR";
            badge = "👑 LEADER";
        } else if (influenceScore > 50) {
            niveau = "INFLUENCEUR ACTIF";
            badge = "⭐ INFLUENT";
        } else if (influenceScore > 20) {
            niveau = "MEMBRE ENGAGÉ";
            badge = "🌟 ACTIF";
        } else {
            niveau = "MEMBRE NOUVEAU";
            badge = "🌱 DÉBUTANT";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("user", user.getFirstName() + " " + user.getLastName());
        result.put("id", userId);
        result.put("influenceScore", Math.round(influenceScore * 100) / 100.0);
        result.put("niveau", niveau);
        result.put("badge", badge);
        result.put("interactionsRecues", interactionsRecues);
        result.put("interactionsEnvoyees", interactionsEnvoyees);
        result.put("participations", participations);
        result.put("centresInteret", tagsCount);

        return result;
    }

    public Interaction createInteraction(String sourceId, String cibleId, String type, String contenu) {
        User source = userRepository.findById(sourceId).orElse(null);
        User cible = userRepository.findById(cibleId).orElse(null);

        if (source == null || cible == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        int poids;
        switch (type) {
            case "LIKE":
                poids = 1;
                break;
            case "COMMENT":
                poids = 2;
                break;
            case "COLLABORATION":
                poids = 5;
                break;
            case "MENTION":
                poids = 1;
                break;
            default:
                poids = 1;
        }

        Interaction interaction = Interaction.builder()
                .userSource(source)
                .userCible(cible)
                .type(type)
                .contenu(contenu)
                .dateInteraction(LocalDateTime.now())
                .poids(poids)
                .build();

        return interactionRepository.save(interaction);
    }

    public List<Map<String, Object>> detectCommunities() {
        List<User> allUsers = userRepository.findAll();
        Map<String, List<String>> graph = new HashMap<>();

        for (User u : allUsers) {
            List<String> connectedIds = userRepository.findConnectedUsers(u.getId())
                    .stream().map(User::getId).collect(Collectors.toList());
            graph.put(u.getId(), connectedIds);
        }

        List<List<User>> communities = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (User u : allUsers) {
            if (!visited.contains(u.getId())) {
                List<User> community = new ArrayList<>();
                Queue<User> queue = new LinkedList<>();
                queue.add(u);
                visited.add(u.getId());

                while (!queue.isEmpty()) {
                    User current = queue.poll();
                    community.add(current);

                    for (String neighborId : graph.getOrDefault(current.getId(), new ArrayList<>())) {
                        if (!visited.contains(neighborId)) {
                            User neighbor = userRepository.findById(neighborId).orElse(null);
                            if (neighbor != null) {
                                visited.add(neighborId);
                                queue.add(neighbor);
                            }
                        }
                    }
                }
                if (community.size() > 1) {
                    communities.add(community);
                }
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < communities.size(); i++) {
            Map<String, Object> communityMap = new HashMap<>();
            communityMap.put("id", i + 1);
            communityMap.put("taille", communities.get(i).size());

            List<Map<String, Object>> usersList = new ArrayList<>();
            for (User u : communities.get(i)) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", u.getId());
                userMap.put("nom", u.getFirstName() + " " + u.getLastName());
                usersList.add(userMap);
            }
            communityMap.put("users", usersList);
            result.add(communityMap);
        }

        return result;
    }

    public List<Map<String, Object>> findUsersByInterest(String tag) {
        List<User> users = membreTagRepository.findUsersByTag(tag);

        List<Map<String, Object>> result = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", u.getId());
            item.put("nom", u.getFirstName() + " " + u.getLastName());
            item.put("email", u.getEmail());
            item.put("club", u.getClub() != null ? u.getClub().getNom() : "Aucun");
            result.add(item);
        }

        return result;
    }
}