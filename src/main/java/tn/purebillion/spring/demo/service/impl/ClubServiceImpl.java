package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.enums.StatutClub;
import tn.purebillion.spring.demo.repository.ClubRepository;
import tn.purebillion.spring.demo.service.interfaces.IClubService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClubServiceImpl implements IClubService {

    ClubRepository clubRepository;

    @Override
    public List<Club> retrieveAllClubs() {
        return clubRepository.findAll();
    }

    @Override
    public Club addClub(Club club) {
        club.setDateCreation(LocalDate.now());
        club.setStatut(StatutClub.EN_ATTENTE);
        return clubRepository.save(club);
    }

    @Override
    public Club updateClub(Club club) {
        return clubRepository.save(club);
    }

    @Override
    public Club retrieveClub(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    @Override
    public void removeClub(Long id) {
        clubRepository.deleteById(id);
    }
}