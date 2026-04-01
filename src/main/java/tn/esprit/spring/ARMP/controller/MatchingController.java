package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.dto.MentorMatchDTO;
import tn.esprit.spring.ARMP.service.advanced.IntelligentMatchingService;

import java.util.List;

@RestController
@RequestMapping("/matching")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MatchingController {

    private final IntelligentMatchingService matchingService;

    @GetMapping("/mentors/{userId}")
    public ResponseEntity<List<MentorMatchDTO>> findBestMentors(
            @PathVariable String userId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(matchingService.findBestMentors(userId, limit));
    }

    @GetMapping("/mentees/{userId}")
    public ResponseEntity<List<MentorMatchDTO>> findBestMentees(
            @PathVariable String userId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(matchingService.findBestMentees(userId, limit));
    }
}