package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.dto.ActivitySuggestionDTO;
import tn.esprit.spring.ARMP.service.advanced.IntelligentActivityGeneratorService;

import java.util.List;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ActivityGeneratorController {

    private final IntelligentActivityGeneratorService aiService;

    @GetMapping("/activities/suggest/{clubId}")
    public ResponseEntity<List<ActivitySuggestionDTO>> suggestActivities(@PathVariable Long clubId) {
        return ResponseEntity.ok(aiService.generateIntelligentActivities(clubId));
    }
}