package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Participation;

import java.util.List;

public interface IParticipationService {
    List<Participation> retrieveAllParticipations();
    Participation addParticipation(Participation participation);
    Participation updateParticipation(Participation participation);
    Participation retrieveParticipation(Long id);
    void removeParticipation(Long id);
}