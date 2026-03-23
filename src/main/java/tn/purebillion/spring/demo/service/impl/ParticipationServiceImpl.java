package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Activite;
import tn.purebillion.spring.demo.entity.Event;
import tn.purebillion.spring.demo.entity.Membre;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.enums.StatutPresence;
import tn.purebillion.spring.demo.repository.ActiviteRepository;
import tn.purebillion.spring.demo.repository.EventRepository;
import tn.purebillion.spring.demo.repository.MembreRepository;
import tn.purebillion.spring.demo.repository.ParticipationRepository;
import tn.purebillion.spring.demo.service.interfaces.IParticipationService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipationServiceImpl implements IParticipationService {

    ParticipationRepository participationRepository;
    MembreRepository membreRepository;      // ← AJOUTER
    ActiviteRepository activiteRepository;  // ← AJOUTER
    EventRepository eventRepository;        // ← AJOUTER

    @Override
    public List<Participation> retrieveAllParticipations() {
        return participationRepository.findAll();
    }

    @Override
    public Participation addParticipation(Participation participation) {

        // 1. VÉRIFIER ET RÉCUPÉRER LE MEMBRE
        if (participation.getMembre() == null || participation.getMembre().getIdMembre() == null) {
            throw new RuntimeException("Le membre est requis");
        }

        Long membreId = participation.getMembre().getIdMembre();
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec ID: " + membreId));
        participation.setMembre(membre);

        // 2. VÉRIFIER SI C'EST UNE PARTICIPATION À UNE ACTIVITÉ
        if (participation.getActivite() != null && participation.getActivite().getIdActivite() != null) {
            Long activiteId = participation.getActivite().getIdActivite();
            Activite activite = activiteRepository.findById(activiteId)
                    .orElseThrow(() -> new RuntimeException("Activité non trouvée avec ID: " + activiteId));
            participation.setActivite(activite);
            participation.setEvent(null); // S'assurer que event est null
        }
        // 3. VÉRIFIER SI C'EST UNE PARTICIPATION À UN ÉVÉNEMENT
        else if (participation.getEvent() != null && participation.getEvent().getIdEvent() != null) {
            Long eventId = participation.getEvent().getIdEvent();
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé avec ID: " + eventId));
            participation.setEvent(event);
            participation.setActivite(null); // S'assurer que activite est null
        }
        // 4. AUCUNE ACTIVITÉ NI ÉVÉNEMENT SPÉCIFIÉ
        else {
            throw new RuntimeException("Soit une activité (idActivite), soit un événement (idEvent) est requis");
        }

        // 5. DÉFINIR LA DATE D'INSCRIPTION
        participation.setDateInscription(LocalDate.now());

        // 6. DÉFINIR LE STATUT PAR DÉFAUT SI NÉCESSAIRE
        if (participation.getStatutPresence() == null) {
            participation.setStatutPresence(StatutPresence.INSCRIT);
        }

        // 7. SAUVEGARDER
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

    @Override
    public List<Participation> findByStatutPresence(StatutPresence statutPresence) {
        return participationRepository.findByStatutPresence(statutPresence);
    }

    @Override
    public List<Participation> findByMembreId(Long membreId) {
        return participationRepository.findByMembreIdMembre(membreId);
    }

    @Override
    public List<Participation> findByActiviteId(Long activiteId) {
        return participationRepository.findByActiviteIdActivite(activiteId);
    }

    @Override
    public List<Participation> findByEventId(Long eventId) {
        return participationRepository.findByEventIdEvent(eventId);
    }

    @Override
    public List<Participation> searchParticipations(Long membreId, StatutPresence statutPresence) {
        return participationRepository.searchParticipations(membreId, statutPresence);
    }

    @Override
    public List<Participation> searchAll(Long membreId, Long activiteId, Long eventId,
                                         StatutPresence statutPresence, LocalDate dateStart, LocalDate dateEnd) {
        return participationRepository.searchAll(membreId, activiteId, eventId, statutPresence, dateStart, dateEnd);
    }

    @Override
    public List<Participation> findByMembreIdAndActiviteId(Long membreId, Long activiteId) {
        return participationRepository.findByMembreIdAndActiviteId(membreId, activiteId);
    }

    @Override
    public List<Participation> findByMembreIdAndEventId(Long membreId, Long eventId) {
        return participationRepository.findByMembreIdAndEventId(membreId, eventId);
    }

    @Override
    public long countPresencesByActiviteId(Long activiteId) {
        return participationRepository.countPresencesByActiviteId(activiteId);
    }

    @Override
    public long countPresencesByEventId(Long eventId) {
        return participationRepository.countPresencesByEventId(eventId);
    }
}