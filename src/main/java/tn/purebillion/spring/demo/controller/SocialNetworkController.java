package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Interaction;
import tn.purebillion.spring.demo.service.advanced.SocialNetworkService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/social")
@AllArgsConstructor
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;

    @GetMapping("/recommendations/{membreId}")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations(@PathVariable Long membreId) {
        return ResponseEntity.ok(socialNetworkService.recommendConnections(membreId));
    }

    @GetMapping("/influence/{membreId}")
    public ResponseEntity<Map<String, Object>> getInfluenceScore(@PathVariable Long membreId) {
        return ResponseEntity.ok(socialNetworkService.calculateInfluenceScore(membreId));
    }

    @GetMapping("/communities")
    public ResponseEntity<List<Map<String, Object>>> getCommunities() {
        return ResponseEntity.ok(socialNetworkService.detectCommunities());
    }

    @PostMapping("/interaction")
    public ResponseEntity<Interaction> createInteraction(
            @RequestParam Long sourceId,
            @RequestParam Long cibleId,
            @RequestParam String type,
            @RequestParam(required = false) String contenu) {
        return ResponseEntity.ok(socialNetworkService.createInteraction(sourceId, cibleId, type, contenu));
    }

    @GetMapping("/interests/{tag}")
    public ResponseEntity<List<Map<String, Object>>> findMembresByInterest(@PathVariable String tag) {
        return ResponseEntity.ok(socialNetworkService.findMembresByInterest(tag));
    }
}