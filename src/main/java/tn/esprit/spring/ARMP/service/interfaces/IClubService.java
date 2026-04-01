package tn.esprit.spring.ARMP.service.interfaces;

import tn.esprit.spring.ARMP.entity.Club;
import tn.esprit.spring.ARMP.enums.DomaineClub;
import tn.esprit.spring.ARMP.enums.StatutClub;

import java.time.LocalDate;
import java.util.List;

public interface IClubService {
    List<Club> retrieveAllClubs();
    Club addClub(Club club);
    Club updateClub(Club club);
    Club retrieveClub(Long id);
    void removeClub(Long id);

    List<Club> findByNomContaining(String nom);
    List<Club> findByDomaine(DomaineClub domaine);
    List<Club> findByStatut(StatutClub statut);
    List<Club> findByDateCreationBetween(LocalDate start, LocalDate end);
    List<Club> searchClubs(String nom, DomaineClub domaine, StatutClub statut);
    List<Club> searchAll(String nom, String sigle, String description, DomaineClub domaine,
                         StatutClub statut, LocalDate dateStart, LocalDate dateEnd);
    List<Club> findAllOrderByNomAsc();
    List<Club> findAllOrderByDateCreationDesc();
}