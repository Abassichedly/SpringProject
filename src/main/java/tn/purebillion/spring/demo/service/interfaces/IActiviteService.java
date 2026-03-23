package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Activite;
import tn.purebillion.spring.demo.enums.StatutActivite;
import tn.purebillion.spring.demo.enums.TypeActivite;

import java.time.LocalDate;
import java.util.List;

public interface IActiviteService {
    List<Activite> retrieveAllActivites();
    Activite addActivite(Activite activite);
    Activite updateActivite(Activite activite);
    Activite retrieveActivite(Long id);
    void removeActivite(Long id);

    List<Activite> findByTitreContaining(String titre);
    List<Activite> findByType(TypeActivite type);
    List<Activite> findByLieuContaining(String lieu);
    List<Activite> findByStatut(StatutActivite statut);
    List<Activite> findByClubId(Long clubId);
    List<Activite> findByDate(LocalDate date);
    List<Activite> searchActivites(String titre, TypeActivite type, String lieu);
    List<Activite> searchAll(String titre, TypeActivite type, String lieu, StatutActivite statut,
                             Long clubId, LocalDate dateStart, LocalDate dateEnd);
    List<Activite> findAllOrderByDateAsc();
    List<Activite> findByClubIdOrderByDateDesc(Long clubId);
}