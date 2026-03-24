package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Interaction;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.MembreTag;
import tn.purebillion.spring.demo.repository.InteractionRepository;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.repository.MembreTagRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SocialNetworkService {

    private final MembreRepository membreRepository;
    private final InteractionRepository interactionRepository;
    private final MembreTagRepository membreTagRepository;

    // 1. Recommandation de connexions
    public List<Map<String, Object>> recommendConnections(Long membreId) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) return new ArrayList<>();

        Map<Membre, Integer> scores = new HashMap<>();
        List<String> myTags = membreTagRepository.findTagsByMembreId(membreId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        for (Membre other : membreRepository.findAll()) {
            if (other.getIdMembre().equals(membreId)) continue;

            int score = 0;

            if (membre.getClub() != null && membre.getClub().equals(other.getClub())) {
                score += 50;
            }

            List<String> otherTags = membreTagRepository.findTagsByMembreId(other.getIdMembre())
                    .stream().map(MembreTag::getTag).collect(Collectors.toList());
            long commonTags = myTags.stream().filter(otherTags::contains).count();
            score += (int) commonTags * 15;

            if (score > 0) {
                scores.put(other, score);
            }
        }

        // FIX: Utiliser Map<String, Object> correctement
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Membre, Integer> entry : scores.entrySet().stream()
                .sorted(Map.Entry.<Membre, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList())) {
            Map<String, Object> item = new HashMap<>();
            item.put("membre", entry.getKey().getNom() + " " + entry.getKey().getPrenom());
            item.put("id", entry.getKey().getIdMembre());
            item.put("score", entry.getValue());
            item.put("club", entry.getKey().getClub() != null ? entry.getKey().getClub().getNom() : "Aucun");
            result.add(item);
        }

        return result;
    }

    // 2. Score d'influence
    public Map<String, Object> calculateInfluenceScore(Long membreId) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Membre non trouvé");
            return error;
        }

        long interactionsRecues = interactionRepository.countByMembreCibleId(membreId);
        long interactionsEnvoyees = interactionRepository.countByMembreSourceId(membreId);
        int participations = membre.getParticipations().size();
        int tagsCount = membreTagRepository.findTagsByMembreId(membreId).size();

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

        // FIX: Utiliser HashMap explicite
        Map<String, Object> result = new HashMap<>();
        result.put("membre", membre.getNom() + " " + membre.getPrenom());
        result.put("id", membreId);
        result.put("influenceScore", Math.round(influenceScore * 100) / 100.0);
        result.put("niveau", niveau);
        result.put("badge", badge);
        result.put("interactionsRecues", interactionsRecues);
        result.put("interactionsEnvoyees", interactionsEnvoyees);
        result.put("participations", participations);
        result.put("centresInteret", tagsCount);

        return result;
    }

    // 3. Créer une interaction
    public Interaction createInteraction(Long sourceId, Long cibleId, String type, String contenu) {
        Membre source = membreRepository.findById(sourceId).orElse(null);
        Membre cible = membreRepository.findById(cibleId).orElse(null);

        if (source == null || cible == null) {
            throw new RuntimeException("Membre non trouvé");
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
                .membreSource(source)
                .membreCible(cible)
                .type(type)
                .contenu(contenu)
                .dateInteraction(LocalDateTime.now())
                .poids(poids)
                .build();

        return interactionRepository.save(interaction);
    }

    // 4. Détecter les communautés
    public List<Map<String, Object>> detectCommunities() {
        List<Membre> allMembres = membreRepository.findAll();
        Map<Long, List<Long>> graph = new HashMap<>();

        for (Membre m : allMembres) {
            List<Long> connectedIds = membreRepository.findConnectedMembres(m.getIdMembre())
                    .stream().map(Membre::getIdMembre).collect(Collectors.toList());
            graph.put(m.getIdMembre(), connectedIds);
        }

        List<List<Membre>> communities = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        for (Membre m : allMembres) {
            if (!visited.contains(m.getIdMembre())) {
                List<Membre> community = new ArrayList<>();
                Queue<Membre> queue = new LinkedList<>();
                queue.add(m);
                visited.add(m.getIdMembre());

                while (!queue.isEmpty()) {
                    Membre current = queue.poll();
                    community.add(current);

                    for (Long neighborId : graph.getOrDefault(current.getIdMembre(), new ArrayList<>())) {
                        if (!visited.contains(neighborId)) {
                            Membre neighbor = membreRepository.findById(neighborId).orElse(null);
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

            List<Map<String, Object>> membresList = new ArrayList<>();
            for (Membre m : communities.get(i)) {
                Map<String, Object> membreMap = new HashMap<>();
                membreMap.put("id", m.getIdMembre());
                membreMap.put("nom", m.getNom() + " " + m.getPrenom());
                membresList.add(membreMap);
            }
            communityMap.put("membres", membresList);
            result.add(communityMap);
        }

        return result;
    }

    // 5. Trouver les membres par centre d'intérêt
    public List<Map<String, Object>> findMembresByInterest(String tag) {
        List<Membre> membres = membreTagRepository.findMembresByTags(List.of(tag));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Membre m : membres) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", m.getIdMembre());
            item.put("nom", m.getNom() + " " + m.getPrenom());
            item.put("email", m.getEmail());
            item.put("club", m.getClub() != null ? m.getClub().getNom() : "Aucun");
            result.add(item);
        }

        return result;
    }
}