package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.enums.RoleMembre;
import tn.purebillion.spring.demo.service.interfaces.IMembreService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("membre")
public class MembreController {

    IMembreService membreService;

    @GetMapping("/listMembre")
    public List<Membre> retrieveAllMembres() {
        return membreService.retrieveAllMembres();
    }

    @PostMapping("/add")
    public Membre addMembre(@RequestBody Membre membre) {
        return membreService.addMembre(membre);
    }

    @PutMapping("/update")
    public Membre updateMembre(@RequestBody Membre membre) {
        return membreService.updateMembre(membre);
    }

    @GetMapping("/getbyid/{idMembre}")
    public Membre retrieveMembre(@PathVariable Long idMembre) {
        return membreService.retrieveMembre(idMembre);
    }

    @DeleteMapping("/delete/{idMembre}")
    public void removeMembre(@PathVariable Long idMembre) {
        membreService.removeMembre(idMembre);
    }
    @GetMapping("/search/nom/{nom}")
    public List<Membre> findByNomContaining(@PathVariable String nom) {
        return membreService.findByNomContaining(nom);
    }

    @GetMapping("/search/prenom/{prenom}")
    public List<Membre> findByPrenomContaining(@PathVariable String prenom) {
        return membreService.findByPrenomContaining(prenom);
    }

    @GetMapping("/search/email/{email}")
    public List<Membre> findByEmailContaining(@PathVariable String email) {
        return membreService.findByEmailContaining(email);
    }

    @GetMapping("/search/role/{role}")
    public List<Membre> findByRole(@PathVariable RoleMembre role) {
        return membreService.findByRole(role);
    }

    @GetMapping("/search/actif/{actif}")
    public List<Membre> findByEstActif(@PathVariable Boolean actif) {
        return membreService.findByEstActif(actif);
    }

    @GetMapping("/search/club/{clubId}")
    public List<Membre> findByClubId(@PathVariable Long clubId) {
        return membreService.findByClubId(clubId);
    }

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @GetMapping("/search")
    public List<Membre> searchMembres(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) RoleMembre role) {
        return membreService.searchMembres(nom, prenom, role);
    }

    @GetMapping("/search/all")
    public List<Membre> searchAll(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) RoleMembre role,
            @RequestParam(required = false) Boolean estActif,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
        return membreService.searchAll(nom, prenom, email, role, estActif, clubId, dateStart, dateEnd);
    }

    // ========== TRI ==========
    @GetMapping("/sorted/name")
    public List<Membre> findAllOrderByNomAsc() {
        return membreService.findAllOrderByNomAsc();
    }

    @GetMapping("/sorted/date")
    public List<Membre> findAllOrderByDateAdhesionDesc() {
        return membreService.findAllOrderByDateAdhesionDesc();
    }

    // ========== SPÉCIFIQUE ==========
    @GetMapping("/search/club/{clubId}/actifs")
    public List<Membre> findByClubIdAndEstActifTrue(@PathVariable Long clubId) {
        return membreService.findByClubIdAndEstActifTrue(clubId);
    }

    @GetMapping("/search/club/{clubId}/role/{role}")
    public List<Membre> findByClubIdAndRole(@PathVariable Long clubId, @PathVariable RoleMembre role) {
        return membreService.findByClubIdAndRole(clubId, role);
    }

    @GetMapping("/search/withAtLeast/{minParticipations}")
    public List<Membre> findMembresWithAtLeastNParticipations(@PathVariable int minParticipations) {
        return membreService.findMembresWithAtLeastNParticipations(minParticipations);
    }
}