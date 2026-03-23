package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Activite;
import tn.purebillion.spring.demo.enums.StatutActivite;
import tn.purebillion.spring.demo.repository.ActiviteRepository;
import tn.purebillion.spring.demo.service.interfaces.IActiviteService;

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
}