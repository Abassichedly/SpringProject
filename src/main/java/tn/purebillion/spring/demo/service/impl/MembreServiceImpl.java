package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.enums.RoleMembre;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.service.interfaces.IMembreService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MembreServiceImpl implements IMembreService {

    MembreRepository membreRepository;

    @Override
    public List<Membre> retrieveAllMembres() {
        return membreRepository.findAll();
    }

    @Override
    public Membre addMembre(Membre membre) {
        membre.setDateAdhesion(LocalDate.now());
        membre.setEstActif(true);
        membre.setRole(RoleMembre.MEMBRE_SIMPLE);
        return membreRepository.save(membre);
    }

    @Override
    public Membre updateMembre(Membre membre) {
        return membreRepository.save(membre);
    }

    @Override
    public Membre retrieveMembre(Long id) {
        return membreRepository.findById(id).orElse(null);
    }

    @Override
    public void removeMembre(Long id) {
        membreRepository.deleteById(id);
    }
    @Override
    public List<Membre> findByNomContaining(String nom) {
        return membreRepository.findByNomContaining(nom);
    }

    @Override
    public List<Membre> findByPrenomContaining(String prenom) {
        return membreRepository.findByPrenomContaining(prenom);
    }

    @Override
    public List<Membre> findByEmailContaining(String email) {
        return membreRepository.findByEmailContaining(email);
    }

    @Override
    public List<Membre> findByRole(RoleMembre role) {
        return membreRepository.findByRole(role);
    }

    @Override
    public List<Membre> findByEstActif(Boolean estActif) {
        return membreRepository.findByEstActif(estActif);
    }

    @Override
    public List<Membre> findByClubId(Long clubId) {
        return membreRepository.findByClubIdClub(clubId);
    }

    @Override
    public List<Membre> searchMembres(String nom, String prenom, RoleMembre role) {
        return membreRepository.searchMembres(nom, prenom, role);
    }

    @Override
    public List<Membre> searchAll(String nom, String prenom, String email, RoleMembre role,
                                  Boolean estActif, Long clubId, LocalDate dateStart, LocalDate dateEnd) {
        return membreRepository.searchAll(nom, prenom, email, role, estActif, clubId, dateStart, dateEnd);
    }

    @Override
    public List<Membre> findAllOrderByNomAsc() {
        return membreRepository.findAllByOrderByNomAsc();
    }

    @Override
    public List<Membre> findAllOrderByDateAdhesionDesc() {
        return membreRepository.findAllByOrderByDateAdhesionDesc();
    }

    @Override
    public List<Membre> findByClubIdAndEstActifTrue(Long clubId) {
        return membreRepository.findByClubIdAndEstActifTrue(clubId);
    }

    @Override
    public List<Membre> findByClubIdAndRole(Long clubId, RoleMembre role) {
        return membreRepository.findByClubIdAndRole(clubId, role);
    }

    @Override
    public List<Membre> findMembresWithAtLeastNParticipations(int minParticipations) {
        return membreRepository.findMembresWithAtLeastNParticipations(minParticipations);
    }
}