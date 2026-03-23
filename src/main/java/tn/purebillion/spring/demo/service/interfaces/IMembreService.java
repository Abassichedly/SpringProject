package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Membre;

import java.util.List;

public interface IMembreService {
    List<Membre> retrieveAllMembres();
    Membre addMembre(Membre membre);
    Membre updateMembre(Membre membre);
    Membre retrieveMembre(Long id);
    void removeMembre(Long id);
}