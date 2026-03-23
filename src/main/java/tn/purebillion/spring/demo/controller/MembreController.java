package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.service.interfaces.IMembreService;

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
}