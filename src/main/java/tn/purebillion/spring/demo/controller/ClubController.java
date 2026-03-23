package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.service.interfaces.IClubService;

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
}