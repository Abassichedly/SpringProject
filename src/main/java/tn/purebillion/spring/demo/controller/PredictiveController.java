package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.service.advanced.PredictiveAnalyticsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/predict")
@AllArgsConstructor
public class PredictiveController {

    private final PredictiveAnalyticsService predictiveService;

    @GetMapping("/club/{clubId}")
    public ResponseEntity<Map<String, Object>> predictClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(predictiveService.predictClubEvolution(clubId));
    }

    @GetMapping("/churn/{membreId}")
    public ResponseEntity<Map<String, Object>> predictChurn(@PathVariable Long membreId) {
        return ResponseEntity.ok(predictiveService.predictChurnRisk(membreId));
    }

    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getTrends() {
        return ResponseEntity.ok(predictiveService.predictActivityTrends());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> getClubRanking() {
        return ResponseEntity.ok(predictiveService.rankClubsByPotential());
    }

    @GetMapping("/history/{clubId}")
    public ResponseEntity<List<Map<String, Object>>> getPredictionHistory(@PathVariable Long clubId) {
        return ResponseEntity.ok(predictiveService.getPredictionHistory(clubId));
    }
}