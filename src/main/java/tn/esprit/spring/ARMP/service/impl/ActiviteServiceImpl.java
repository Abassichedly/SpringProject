package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Activite;
import tn.esprit.spring.ARMP.enums.StatutActivite;
import tn.esprit.spring.ARMP.enums.TypeActivite;
import tn.esprit.spring.ARMP.repository.ActiviteRepository;
import tn.esprit.spring.ARMP.service.interfaces.IActiviteService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ActiviteServiceImpl implements IActiviteService {

    ActiviteRepository activiteRepository;

    @Override
    public List<Activite> retrieveAllActivites() {
        return activiteRepository.findAll();
    }

    @Override
    public Activite addActivite(Activite activite) {
        activite.setStatut(StatutActivite.PLANIFIEE);
        return activiteRepository.save(activite);
    }

    @Override
    public Activite updateActivite(Activite activite) {
        return activiteRepository.save(activite);
    }

    @Override
    public Activite retrieveActivite(Long id) {
        return activiteRepository.findById(id).orElse(null);
    }

    @Override
    public void removeActivite(Long id) {
        activiteRepository.deleteById(id);
    }

    @Override
    public List<Activite> findByTitreContaining(String titre) {
        return activiteRepository.findByTitreContaining(titre);
    }

    @Override
    public List<Activite> findByType(TypeActivite type) {
        return activiteRepository.findByType(type);
    }

    @Override
    public List<Activite> findByLieuContaining(String lieu) {
        return activiteRepository.findByLieuContaining(lieu);
    }

    @Override
    public List<Activite> findByStatut(StatutActivite statut) {
        return activiteRepository.findByStatut(statut);
    }

    @Override
    public List<Activite> findByClubId(Long clubId) {
        return activiteRepository.findByClubIdClub(clubId);
    }

    @Override
    public List<Activite> findByDate(LocalDate date) {
        return activiteRepository.findByDate(date);
    }

    @Override
    public List<Activite> searchActivites(String titre, TypeActivite type, String lieu) {
        return activiteRepository.searchActivites(titre, type, lieu);
    }

    @Override
    public List<Activite> searchAll(String titre, TypeActivite type, String lieu, StatutActivite statut,
                                    Long clubId, LocalDate dateStart, LocalDate dateEnd) {
        return activiteRepository.searchAll(titre, type, lieu, statut, clubId, dateStart, dateEnd);
    }

    @Override
    public List<Activite> findAllOrderByDateAsc() {
        return activiteRepository.findAllByOrderByDateAsc();
    }

    @Override
    public List<Activite> findByClubIdOrderByDateDesc(Long clubId) {
        return activiteRepository.findByClubIdOrderByDateDesc(clubId);
    }
}