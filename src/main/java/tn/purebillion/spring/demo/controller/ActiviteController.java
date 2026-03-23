package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Activite;
import tn.purebillion.spring.demo.service.interfaces.IActiviteService;

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
}