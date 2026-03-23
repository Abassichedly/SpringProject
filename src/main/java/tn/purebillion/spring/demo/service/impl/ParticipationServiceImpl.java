package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.repository.ParticipationRepository;
import tn.purebillion.spring.demo.service.interfaces.IParticipationService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipationServiceImpl implements IParticipationService {

    ParticipationRepository participationRepository;

    @Override
    public List<Participation> retrieveAllParticipations() {
        return participationRepository.findAll();
    }

    @Override
    public Participation addParticipation(Participation participation) {
        participation.setDateInscription(LocalDate.now());
        return participationRepository.save(participation);
    }

    @Override
    public Participation updateParticipation(Participation participation) {
        return participationRepository.save(participation);
    }

    @Override
    public Participation retrieveParticipation(Long id) {
        return participationRepository.findById(id).orElse(null);
    }

    @Override
    public void removeParticipation(Long id) {
        participationRepository.deleteById(id);
    }
}