package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.service.advanced.InsightAnalyticsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insights")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class InsightController {

    private final InsightAnalyticsService insightService;

    @GetMapping("/heatmap")
    public ResponseEntity<Map<String, Object>> getHeatmap() {
        return ResponseEntity.ok(insightService.getActivityHeatmap());
    }

    @GetMapping("/club/{clubId}/health")
    public ResponseEntity<Map<String, Object>> getClubHealth(@PathVariable Long clubId) {
        return ResponseEntity.ok(insightService.getClubHealthReport(clubId));
    }

    @GetMapping("/compare")
    public ResponseEntity<List<Map<String, Object>>> compareClubs() {
        return ResponseEntity.ok(insightService.compareClubs());
    }

    @GetMapping("/interests/top")
    public ResponseEntity<List<Map<String, Object>>> getTopInterests() {
        return ResponseEntity.ok(insightService.getTopInterests());
    }
}