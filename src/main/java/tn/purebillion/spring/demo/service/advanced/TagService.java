package tn.purebillion.spring.demo.service.advanced;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.MembreTag;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.repository.MembreTagRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {

    private final MembreTagRepository membreTagRepository;
    private final MembreRepository membreRepository;

    // 1. AJOUTER UN TAG À UN MEMBRE
    public MembreTag addTagToMembre(Long membreId, String tag, Integer poids) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) {
            throw new RuntimeException("Membre non trouvé");
        }

        MembreTag membreTag = MembreTag.builder()
                .membre(membre)
                .tag(tag.toUpperCase())
                .poids(poids != null ? poids : 5)
                .dateAjout(LocalDateTime.now())
                .build();

        return membreTagRepository.save(membreTag);
    }

    // 2. AJOUTER PLUSIEURS TAGS
    public List<MembreTag> addTagsToMembre(Long membreId, List<String> tags) {
        List<MembreTag> savedTags = new ArrayList<>();
        for (String tag : tags) {
            savedTags.add(addTagToMembre(membreId, tag, 5));
        }
        return savedTags;
    }

    // 3. TROUVER LES MEMBRES PAR TAG
    public List<Map<String, Object>> findMembresByTag(String tag) {
        List<Membre> membres = membreTagRepository.findMembresByTag(tag.toUpperCase());
        return membres.stream().map(m -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", m.getIdMembre());
            result.put("nom", m.getNom() + " " + m.getPrenom());
            result.put("email", m.getEmail());
            result.put("club", m.getClub() != null ? m.getClub().getNom() : null);
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
    public List<String> recommendTags(Long membreId) {
        Membre membre = membreRepository.findById(membreId).orElse(null);
        if (membre == null) return new ArrayList<>();

        List<String> myTags = membreTagRepository.findByMembreId(membreId)
                .stream().map(MembreTag::getTag).collect(Collectors.toList());

        // Trouver des membres similaires
        List<Membre> allMembres = membreRepository.findAll();
        Map<Membre, Long> similarMembres = new HashMap<>();

        for (Membre other : allMembres) {
            if (other.getIdMembre().equals(membreId)) continue;

            List<String> otherTags = membreTagRepository.findByMembreId(other.getIdMembre())
                    .stream().map(MembreTag::getTag).collect(Collectors.toList());

            long commonTags = myTags.stream().filter(otherTags::contains).count();
            if (commonTags > 0) {
                similarMembres.put(other, commonTags);
            }
        }

        // Collecter les tags des membres similaires
        Map<String, Integer> tagScores = new HashMap<>();
        for (Membre similar : similarMembres.keySet()) {
            List<MembreTag> tags = membreTagRepository.findByMembreId(similar.getIdMembre());
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
    public Map<String, Object> getMemberTagProfile(Long membreId) {
        List<MembreTag> tags = membreTagRepository.findByMembreId(membreId);
        Membre membre = membreRepository.findById(membreId).orElse(null);

        List<Map<String, Object>> tagList = tags.stream().map(t -> {
            Map<String, Object> tagInfo = new HashMap<>();
            tagInfo.put("tag", t.getTag());
            tagInfo.put("poids", t.getPoids());
            return tagInfo;
        }).collect(Collectors.toList());

        return Map.of(
                "membre", membre != null ? membre.getNom() + " " + membre.getPrenom() : "Inconnu",
                "tags", tagList,
                "nombreTags", tags.size(),
                "recommandations", recommendTags(membreId)
        );
    }
}