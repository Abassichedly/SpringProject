package tn.esprit.spring.ARMP.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.entity.MembreTag;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.repository.MembreTagRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {

    private final MembreTagRepository membreTagRepository;
    private final UserRepository userRepository;

    // 1. AJOUTER UN TAG À UN MEMBRE
    public MembreTag addTagToUser(String userId, String tag, Integer poids) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        MembreTag membreTag = MembreTag.builder()
                .user(user)
                .tag(tag.toUpperCase())
                .poids(poids != null ? poids : 5)
                .dateAjout(LocalDateTime.now())
                .build();

        return membreTagRepository.save(membreTag);
    }

    // 2. AJOUTER PLUSIEURS TAGS
    public List<MembreTag> addTagsToUser(String userId, List<String> tags) {
        List<MembreTag> savedTags = new ArrayList<>();
        for (String tag : tags) {
            savedTags.add(addTagToUser(userId, tag, 5));
        }
        return savedTags;
    }

    // 3. TROUVER LES MEMBRES PAR TAG
    public List<Map<String, Object>> findUsersByTag(String tag) {
        List<User> users = membreTagRepository.findUsersByTag(tag.toUpperCase());
        return users.stream().map(u -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", u.getId());
            result.put("nom", u.getFirstName() + " " + u.getLastName());
            result.put("email", u.getEmail());
            result.put("club", u.getClub() != null ? u.getClub().getNom() : null);
            return result;
        }).collect(Collectors.toList());
    }

    // 4. TAGS POPULAIRES
    public List<Map<String, Object>> getPopularTags() {
        List<Object[]> popularTags = membreTagRepository.findMostPopularTags();
        return popularTags.stream()
                .limit(10)
                .map(tag -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("tag", tag[0]);
                    result.put("count", tag[1]);
                    return result;
                })
                .collect(Collectors.toList());
    }

    // 5. RECOMMANDER DES TAGS À UN MEMBRE (BASÉ SUR LES MEMBRES SIMILAIRES)
    public List<String> recommendTags(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        List<String> myTags = membreTagRepository.findTagsByUserId(userId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        // Trouver des users similaires
        List<User> allUsers = userRepository.findAll();
        Map<User, Long> similarUsers = new HashMap<>();

        for (User other : allUsers) {
            if (other.getId().equals(userId)) continue;

            List<String> otherTags = membreTagRepository.findTagsByUserId(other.getId())
                    .stream().map(MembreTag::getTag).collect(Collectors.toList());

            long commonTags = myTags.stream().filter(otherTags::contains).count();
            if (commonTags > 0) {
                similarUsers.put(other, commonTags);
            }
        }

        // Collecter les tags des users similaires
        Map<String, Integer> tagScores = new HashMap<>();
        for (User similar : similarUsers.keySet()) {
            List<MembreTag> tags = membreTagRepository.findTagsByUserId(similar.getId());
            for (MembreTag t : tags) {
                if (!myTags.contains(t.getTag())) {
                    tagScores.put(t.getTag(), tagScores.getOrDefault(t.getTag(), 0) + t.getPoids());
                }
            }
        }

        return tagScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 6. PROFIL DE TAGS D'UN MEMBRE
    public Map<String, Object> getUserTagProfile(String userId) {
        List<MembreTag> tags = membreTagRepository.findTagsByUserId(userId);
        User user = userRepository.findById(userId).orElse(null);

        List<Map<String, Object>> tagList = tags.stream().map(t -> {
            Map<String, Object> tagInfo = new HashMap<>();
            tagInfo.put("tag", t.getTag());
            tagInfo.put("poids", t.getPoids());
            return tagInfo;
        }).collect(Collectors.toList());

        return Map.of(
                "user", user != null ? user.getFirstName() + " " + user.getLastName() : "Inconnu",
                "tags", tagList,
                "nombreTags", tags.size(),
                "recommandations", recommendTags(userId)
        );
    }
}