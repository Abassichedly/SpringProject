package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Event;
import tn.purebillion.spring.demo.enums.StatutEvent;
import tn.purebillion.spring.demo.service.interfaces.IEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("event")
public class EventController {

    IEventService eventService;

    @GetMapping("/listEvent")
    public List<Event> retrieveAllEvents() {
        return eventService.retrieveAllEvents();
    }

    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    @PutMapping("/update")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @GetMapping("/getbyid/{idEvent}")
    public Event retrieveEvent(@PathVariable Long idEvent) {
        return eventService.retrieveEvent(idEvent);
    }

    @DeleteMapping("/delete/{idEvent}")
    public void removeEvent(@PathVariable Long idEvent) {
        eventService.removeEvent(idEvent);
    }
    @GetMapping("/search/nom/{nom}")
    public List<Event> findByNomContaining(@PathVariable String nom) {
        return eventService.findByNomContaining(nom);
    }

    // Recherche simple par lieu
    @GetMapping("/search/lieu/{lieu}")
    public List<Event> findByLieuContaining(@PathVariable String lieu) {
        return eventService.findByLieuContaining(lieu);
    }

    // Recherche simple par statut
    @GetMapping("/search/statut/{statut}")
    public List<Event> findByStatut(@PathVariable StatutEvent statut) {
        return eventService.findByStatut(statut);
    }

    // Recherche simple par club
    @GetMapping("/search/club/{clubId}")
    public List<Event> findByClubId(@PathVariable Long clubId) {
        return eventService.findByClubId(clubId);
    }

    // Recherche simple par prix max
    @GetMapping("/search/prix/{prix}")
    public List<Event> findByPrixAdherentLessThan(@PathVariable Double prix) {
        return eventService.findByPrixAdherentLessThan(prix);
    }

    // Recherche avec 2-3 attributs
    @GetMapping("/search")
    public List<Event> searchEvents(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) StatutEvent statut) {
        return eventService.searchEvents(nom, lieu, statut);
    }

    // Recherche avec tous les attributs
    @GetMapping("/search/all")
    public List<Event> searchAll(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) StatutEvent statut,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnd,
            @RequestParam(required = false) Double prixMax) {
        return eventService.searchAll(nom, lieu, statut, clubId, dateStart, dateEnd, prixMax);
    }

    // Tri par date
    @GetMapping("/sorted/date")
    public List<Event> findAllOrderByDateDebutAsc() {
        return eventService.findAllOrderByDateDebutAsc();
    }
}