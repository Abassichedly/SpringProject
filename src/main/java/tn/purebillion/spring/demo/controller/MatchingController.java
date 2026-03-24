package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.dto.MentorMatchDTO;
import tn.purebillion.spring.demo.service.advanced.IntelligentMatchingService;

import java.util.List;

@RestController
@RequestMapping("/matching")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MatchingController {

    private final IntelligentMatchingService matchingService;

    @GetMapping("/mentors/{membreId}")
    public ResponseEntity<List<MentorMatchDTO>> findBestMentors(
            @PathVariable Long membreId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(matchingService.findBestMentors(membreId, limit));
    }

    @GetMapping("/mentees/{mentorId}")
    public ResponseEntity<List<MentorMatchDTO>> findBestMentees(
            @PathVariable Long mentorId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(matchingService.findBestMentees(mentorId, limit));
    }
}