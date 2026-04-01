package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.entity.Interaction;
import tn.esprit.spring.ARMP.service.advanced.SocialNetworkService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/social")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(socialNetworkService.recommendConnections(userId));
    }

    @GetMapping("/influence/{userId}")
    public ResponseEntity<Map<String, Object>> getInfluenceScore(@PathVariable String userId) {
        return ResponseEntity.ok(socialNetworkService.calculateInfluenceScore(userId));
    }

    @GetMapping("/communities")
    public ResponseEntity<List<Map<String, Object>>> getCommunities() {
        return ResponseEntity.ok(socialNetworkService.detectCommunities());
    }

    @PostMapping("/interaction")
    public ResponseEntity<Interaction> createInteraction(
            @RequestParam String sourceId,
            @RequestParam String cibleId,
            @RequestParam String type,
            @RequestParam(required = false) String contenu) {
        return ResponseEntity.ok(socialNetworkService.createInteraction(sourceId, cibleId, type, contenu));
    }

    @GetMapping("/interests/{tag}")
    public ResponseEntity<List<Map<String, Object>>> findUsersByInterest(@PathVariable String tag) {
        return ResponseEntity.ok(socialNetworkService.findUsersByInterest(tag));
    }
}