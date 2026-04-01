package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Club;
import tn.esprit.spring.ARMP.enums.DomaineClub;
import tn.esprit.spring.ARMP.enums.StatutClub;
import tn.esprit.spring.ARMP.repository.ClubRepository;
import tn.esprit.spring.ARMP.service.interfaces.IClubService;

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
    @Override
    public List<Club> findByNomContaining(String nom) {
        return clubRepository.findByNomContaining(nom);
    }

    @Override
    public List<Club> findByDomaine(DomaineClub domaine) {
        return clubRepository.findByDomaine(domaine);
    }

    @Override
    public List<Club> findByStatut(StatutClub statut) {
        return clubRepository.findByStatut(statut);
    }

    @Override
    public List<Club> findByDateCreationBetween(LocalDate start, LocalDate end) {
        return clubRepository.findByDateCreationBetween(start, end);
    }

    @Override
    public List<Club> searchClubs(String nom, DomaineClub domaine, StatutClub statut) {
        return clubRepository.searchClubs(nom, domaine, statut);
    }

    @Override
    public List<Club> searchAll(String nom, String sigle, String description, DomaineClub domaine,
                                StatutClub statut, LocalDate dateStart, LocalDate dateEnd) {
        return clubRepository.searchAll(nom, sigle, description, domaine, statut, dateStart, dateEnd);
    }

    @Override
    public List<Club> findAllOrderByNomAsc() {
        return clubRepository.findAllByOrderByNomAsc();
    }

    @Override
    public List<Club> findAllOrderByDateCreationDesc() {
        return clubRepository.findAllByOrderByDateCreationDesc();
    }
}