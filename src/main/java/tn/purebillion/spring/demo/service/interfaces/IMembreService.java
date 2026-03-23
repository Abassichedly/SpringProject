package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.enums.RoleMembre;

import java.time.LocalDate;
import java.util.List;

public interface IMembreService {
        // CRUD
        List<Membre> retrieveAllMembres();
        Membre addMembre(Membre membre);
        Membre updateMembre(Membre membre);
        Membre retrieveMembre(Long id);
        void removeMembre(Long id);

        // Recherche simple
        List<Membre> findByNomContaining(String nom);
        List<Membre> findByPrenomContaining(String prenom);
        List<Membre> findByEmailContaining(String email);
        List<Membre> findByRole(RoleMembre role);
        List<Membre> findByEstActif(Boolean estActif);
        List<Membre> findByClubId(Long clubId);

        // Recherche multi-critères
        List<Membre> searchMembres(String nom, String prenom, RoleMembre role);
        List<Membre> searchAll(String nom, String prenom, String email, RoleMembre role,
                               Boolean estActif, Long clubId, LocalDate dateStart, LocalDate dateEnd);

        // Tri
        List<Membre> findAllOrderByNomAsc();
        List<Membre> findAllOrderByDateAdhesionDesc();

        // Spécifique
        List<Membre> findByClubIdAndEstActifTrue(Long clubId);
        List<Membre> findByClubIdAndRole(Long clubId, RoleMembre role);
        List<Membre> findMembresWithAtLeastNParticipations(int minParticipations);

}