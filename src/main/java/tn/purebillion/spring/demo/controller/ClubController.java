package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.enums.DomaineClub;
import tn.purebillion.spring.demo.enums.StatutClub;
import tn.purebillion.spring.demo.service.interfaces.IClubService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("club")
public class ClubController {

    IClubService clubService;

    @GetMapping("/listClub")
    public List<Club> retrieveAllClubs() {
        return clubService.retrieveAllClubs();
    }

    @PostMapping("/add")
    public Club addClub(@RequestBody Club club) {
        return clubService.addClub(club);
    }

    @PutMapping("/update")
    public Club updateClub(@RequestBody Club club) {
        return clubService.updateClub(club);
    }

    @GetMapping("/getbyid/{idClub}")
    public Club retrieveClub(@PathVariable Long idClub) {
        return clubService.retrieveClub(idClub);
    }

    @DeleteMapping("/delete/{idClub}")
    public void removeClub(@PathVariable Long idClub) {
        clubService.removeClub(idClub);
    }
    @GetMapping("/search/nom/{nom}")
    public List<Club> findByNomContaining(@PathVariable String nom) {
        return clubService.findByNomContaining(nom);
    }

    @GetMapping("/search/domaine/{domaine}")
    public List<Club> findByDomaine(@PathVariable DomaineClub domaine) {
        return clubService.findByDomaine(domaine);
    }

    @GetMapping("/search/statut/{statut}")
    public List<Club> findByStatut(@PathVariable StatutClub statut) {
        return clubService.findByStatut(statut);
    }

    @GetMapping("/search/date")
    public List<Club> findByDateCreationBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return clubService.findByDateCreationBetween(start, end);
    }

    @GetMapping("/search")
    public List<Club> searchClubs(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) DomaineClub domaine,
            @RequestParam(required = false) StatutClub statut) {
        return clubService.searchClubs(nom, domaine, statut);
    }

    @GetMapping("/search/all")
    public List<Club> searchAll(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String sigle,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) DomaineClub domaine,
            @RequestParam(required = false) StatutClub statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
        return clubService.searchAll(nom, sigle, description, domaine, statut, dateStart, dateEnd);
    }

    @GetMapping("/sorted/name")
    public List<Club> findAllOrderByNomAsc() {
        return clubService.findAllOrderByNomAsc();
    }

    @GetMapping("/sorted/date")
    public List<Club> findAllOrderByDateCreationDesc() {
        return clubService.findAllOrderByDateCreationDesc();
    }
}