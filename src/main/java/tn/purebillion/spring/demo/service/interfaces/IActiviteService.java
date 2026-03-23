package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Activite;

import java.util.List;

public interface IActiviteService {
    List<Activite> retrieveAllActivites();
    Activite addActivite(Activite activite);
    Activite updateActivite(Activite activite);
    Activite retrieveActivite(Long id);
    void removeActivite(Long id);
}