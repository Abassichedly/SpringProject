package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.service.advanced.GamificationAdvancedService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gamification")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class GamificationController {

    private final GamificationAdvancedService gamificationService;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        return ResponseEntity.ok(gamificationService.generateFullLeaderboard());
    }

    @GetMapping("/journey/{userId}")
    public ResponseEntity<Map<String, Object>> getJourney(@PathVariable String userId) {
        return ResponseEntity.ok(gamificationService.getPersonalizedJourney(userId));
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String userId) {
        return ResponseEntity.ok(gamificationService.getUserStats(userId));
    }
}