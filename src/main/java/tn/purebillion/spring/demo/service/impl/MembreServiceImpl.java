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
}