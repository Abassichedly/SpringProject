package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.dto.ChartDataDTO;
import tn.esprit.spring.ARMP.service.advanced.ChartGeneratorService;

import java.util.Map;

@RestController
@RequestMapping("/charts")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ChartController {

    private final ChartGeneratorService chartService;

    @GetMapping("/evolution/{clubId}")
    public ResponseEntity<ChartDataDTO> getEvolutionChart(@PathVariable Long clubId) {
        return ResponseEntity.ok(chartService.generateMemberEvolutionChart(clubId));
    }

    @GetMapping("/skills/{clubId}")
    public ResponseEntity<ChartDataDTO> getSkillsRadar(@PathVariable Long clubId) {
        return ResponseEntity.ok(chartService.generateSkillsRadarChart(clubId));
    }

    @GetMapping("/heatmap/{clubId}")
    public ResponseEntity<ChartDataDTO> getHeatmap(@PathVariable Long clubId) {
        return ResponseEntity.ok(chartService.generateActivityHeatmapChart(clubId));
    }

    @GetMapping("/activity-types/{clubId}")
    public ResponseEntity<ChartDataDTO> getActivityTypes(@PathVariable Long clubId) {
        return ResponseEntity.ok(chartService.generateActivityTypesPieChart(clubId));
    }

    @GetMapping("/dashboard/{clubId}")
    public ResponseEntity<Map<String, ChartDataDTO>> getDashboardCharts(@PathVariable Long clubId) {
        return ResponseEntity.ok(chartService.generateFullDashboardCharts(clubId));
    }
}