package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.service.interfaces.IParticipationService;

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
}