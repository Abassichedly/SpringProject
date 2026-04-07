package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.entity.Activite;
import tn.esprit.spring.ARMP.entity.Event;
import tn.esprit.spring.ARMP.entity.Participation;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.enums.StatutPresence;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.service.interfaces.IParticipationService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("participation")
public class ParticipationController {

    IParticipationService participationService;
    UserRepository userRepository;

    @GetMapping("/listParticipation")
    public List<Participation> retrieveAllParticipations() {
        return participationService.retrieveAllParticipations();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addParticipation(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== RAW PAYLOAD ===");
            System.out.println(payload);

            Participation participation = new Participation();

            // Extract user
            Map<String, String> userMap = (Map<String, String>) payload.get("user");
            if (userMap != null && userMap.get("id") != null) {
                String userId = userMap.get("id");
                participation.setUserId(userId);
                User user = userRepository.findById(userId).orElse(null);
                participation.setUser(user);
            }

            // Extract activity
            if (payload.containsKey("activite")) {
                Map<String, Object> activiteMap = (Map<String, Object>) payload.get("activite");
                if (activiteMap != null && activiteMap.get("idActivite") != null) {
                    Long activiteId = ((Number) activiteMap.get("idActivite")).longValue();
                    Activite activite = new Activite();
                    activite.setIdActivite(activiteId);
                    participation.setActivite(activite);
                }
            }

            // Extract event
            if (payload.containsKey("event")) {
                Map<String, Object> eventMap = (Map<String, Object>) payload.get("event");
                if (eventMap != null && eventMap.get("idEvent") != null) {
                    Long eventId = ((Number) eventMap.get("idEvent")).longValue();
                    Event event = new Event();
                    event.setIdEvent(eventId);
                    participation.setEvent(event);
                }
            }

            // Extract other fields
            if (payload.containsKey("statutPresence")) {
                participation.setStatutPresence(StatutPresence.valueOf(payload.get("statutPresence").toString()));
            }

            if (payload.containsKey("role")) {
                participation.setRole(payload.get("role").toString());
            }

            if (payload.containsKey("dateInscription")) {
                participation.setDateInscription(LocalDate.parse(payload.get("dateInscription").toString()));
            }

            Participation saved = participationService.addParticipation(participation);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/update")
    public Participation updateParticipation(@RequestBody Participation participation) {
        return participationService.updateParticipation(participation);
    }

    @GetMapping("/getbyid/{idParticipation}")
    public Participation retrieveParticipation(@PathVariable Long idParticipation) {
        return participationService.retrieveParticipation(idParticipation);
    }

    @DeleteMapping("/delete/{idParticipation}")
    public void removeParticipation(@PathVariable Long idParticipation) {
        participationService.removeParticipation(idParticipation);
    }

    @GetMapping("/search/statut/{statut}")
    public List<Participation> findByStatutPresence(@PathVariable StatutPresence statut) {
        return participationService.findByStatutPresence(statut);
    }

    @GetMapping("/search/membre/{membreId}")
    public List<Participation> findByMembreId(@PathVariable Long membreId) {
        return participationService.findByMembreId(membreId);
    }

    @GetMapping("/search/activite/{activiteId}")
    public List<Participation> findByActiviteId(@PathVariable Long activiteId) {
        return participationService.findByActiviteId(activiteId);
    }

    @GetMapping("/search/event/{eventId}")
    public List<Participation> findByEventId(@PathVariable Long eventId) {
        return participationService.findByEventId(eventId);
    }

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @GetMapping("/search")
    public List<Participation> searchParticipations(
            @RequestParam(required = false) Long membreId,
            @RequestParam(required = false) StatutPresence statutPresence) {
        return participationService.searchParticipations(membreId, statutPresence);
    }

    @GetMapping("/search/all")
    public List<Participation> searchAll(
            @RequestParam(required = false) Long membreId,
            @RequestParam(required = false) Long activiteId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) StatutPresence statutPresence,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
        return participationService.searchAll(membreId, activiteId, eventId, statutPresence, dateStart, dateEnd);
    }

    // ========== SPÉCIFIQUE ==========
    @GetMapping("/search/membre/{membreId}/activite/{activiteId}")
    public List<Participation> findByMembreIdAndActiviteId(@PathVariable Long membreId, @PathVariable Long activiteId) {
        return participationService.findByMembreIdAndActiviteId(membreId, activiteId);
    }

    @GetMapping("/search/membre/{membreId}/event/{eventId}")
    public List<Participation> findByMembreIdAndEventId(@PathVariable Long membreId, @PathVariable Long eventId) {
        return participationService.findByMembreIdAndEventId(membreId, eventId);
    }

    @GetMapping("/stats/activite/{activiteId}/presences")
    public long countPresencesByActiviteId(@PathVariable Long activiteId) {
        return participationService.countPresencesByActiviteId(activiteId);
    }

    @GetMapping("/stats/event/{eventId}/presences")
    public long countPresencesByEventId(@PathVariable Long eventId) {
        return participationService.countPresencesByEventId(eventId);
    }
}