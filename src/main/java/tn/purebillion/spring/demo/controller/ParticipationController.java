package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.enums.StatutPresence;
import tn.purebillion.spring.demo.service.interfaces.IParticipationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("participation")
public class ParticipationController {

    IParticipationService participationService;

    @GetMapping("/listParticipation")
    public List<Participation> retrieveAllParticipations() {
        return participationService.retrieveAllParticipations();
    }

    @PostMapping("/add")
    public Participation addParticipation(@RequestBody Participation participation) {
        return participationService.addParticipation(participation);
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