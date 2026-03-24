package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.service.advanced.GamificationAdvancedService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gamification")
@AllArgsConstructor
public class GamificationController {

    private final GamificationAdvancedService gamificationService;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        return ResponseEntity.ok(gamificationService.generateFullLeaderboard());
    }

    @GetMapping("/journey/{membreId}")
    public ResponseEntity<Map<String, Object>> getJourney(@PathVariable Long membreId) {
        return ResponseEntity.ok(gamificationService.getPersonalizedJourney(membreId));
    }

    @GetMapping("/stats/{membreId}")
    public ResponseEntity<Map<String, Object>> getMemberStats(@PathVariable Long membreId) {
        return ResponseEntity.ok(gamificationService.getMemberStats(membreId));
    }
}