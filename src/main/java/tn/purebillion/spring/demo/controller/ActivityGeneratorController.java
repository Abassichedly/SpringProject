package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.dto.ActivitySuggestionDTO;
import tn.purebillion.spring.demo.service.advanced.IntelligentActivityGeneratorService;

import java.util.List;

@RestController
@RequestMapping("/ai/activities")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ActivityGeneratorController {

    private final IntelligentActivityGeneratorService generatorService;

    @GetMapping("/suggest/{clubId}")
    public ResponseEntity<List<ActivitySuggestionDTO>> suggestActivities(@PathVariable Long clubId) {
        List<ActivitySuggestionDTO> suggestions = generatorService.generateIntelligentActivities(clubId);
        return ResponseEntity.ok(suggestions);
    }
}