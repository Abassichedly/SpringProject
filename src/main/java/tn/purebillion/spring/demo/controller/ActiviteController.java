package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Activite;
import tn.purebillion.spring.demo.enums.StatutActivite;
import tn.purebillion.spring.demo.enums.TypeActivite;
import tn.purebillion.spring.demo.service.interfaces.IActiviteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("activite")
public class ActiviteController {

    IActiviteService activiteService;

    @GetMapping("/listActivite")
    public List<Activite> retrieveAllActivites() {
        return activiteService.retrieveAllActivites();
    }

    @PostMapping("/add")
    public Activite addActivite(@RequestBody Activite activite) {
        return activiteService.addActivite(activite);
    }

    @PutMapping("/update")
    public Activite updateActivite(@RequestBody Activite activite) {
        return activiteService.updateActivite(activite);
    }

    @GetMapping("/getbyid/{idActivite}")
    public Activite retrieveActivite(@PathVariable Long idActivite) {
        return activiteService.retrieveActivite(idActivite);
    }

    @DeleteMapping("/delete/{idActivite}")
    public void removeActivite(@PathVariable Long idActivite) {
        activiteService.removeActivite(idActivite);
    }

    @GetMapping("/search/titre/{titre}")
    public List<Activite> findByTitreContaining(@PathVariable String titre) {
        return activiteService.findByTitreContaining(titre);
    }

    @GetMapping("/search/type/{type}")
    public List<Activite> findByType(@PathVariable TypeActivite type) {
        return activiteService.findByType(type);
    }

    @GetMapping("/search/lieu/{lieu}")
    public List<Activite> findByLieuContaining(@PathVariable String lieu) {
        return activiteService.findByLieuContaining(lieu);
    }

    @GetMapping("/search/statut/{statut}")
    public List<Activite> findByStatut(@PathVariable StatutActivite statut) {
        return activiteService.findByStatut(statut);
    }

    @GetMapping("/search/club/{clubId}")
    public List<Activite> findByClubId(@PathVariable Long clubId) {
        return activiteService.findByClubId(clubId);
    }

    @GetMapping("/search/date/{date}")
    public List<Activite> findByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return activiteService.findByDate(date);
    }

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @GetMapping("/search")
    public List<Activite> searchActivites(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) TypeActivite type,
            @RequestParam(required = false) String lieu) {
        return activiteService.searchActivites(titre, type, lieu);
    }

    @GetMapping("/search/all")
    public List<Activite> searchAll(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) TypeActivite type,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) StatutActivite statut,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
        return activiteService.searchAll(titre, type, lieu, statut, clubId, dateStart, dateEnd);
    }

    // ========== TRI ==========
    @GetMapping("/sorted/date")
    public List<Activite> findAllOrderByDateAsc() {
        return activiteService.findAllOrderByDateAsc();
    }

    @GetMapping("/sorted/club/{clubId}")
    public List<Activite> findByClubIdOrderByDateDesc(@PathVariable Long clubId) {
        return activiteService.findByClubIdOrderByDateDesc(clubId);
    }
}