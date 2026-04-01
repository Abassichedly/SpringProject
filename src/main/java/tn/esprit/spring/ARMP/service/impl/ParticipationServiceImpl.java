package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Activite;
import tn.esprit.spring.ARMP.entity.Event;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.entity.Participation;
import tn.esprit.spring.ARMP.enums.StatutPresence;
import tn.esprit.spring.ARMP.repository.ActiviteRepository;
import tn.esprit.spring.ARMP.repository.EventRepository;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.repository.ParticipationRepository;
import tn.esprit.spring.ARMP.service.interfaces.IParticipationService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipationServiceImpl implements IParticipationService {

    ParticipationRepository participationRepository;
    UserRepository userRepository;
    ActiviteRepository activiteRepository;
    EventRepository eventRepository;

    @Override
    public List<Participation> retrieveAllParticipations() {
        return participationRepository.findAll();
    }

    @Override
    public Participation addParticipation(Participation participation) {

        // 1. VÉRIFIER ET RÉCUPÉRER L'UTILISATEUR
        if (participation.getUser() == null || participation.getUser().getId() == null) {
            throw new RuntimeException("L'utilisateur est requis");
        }

        String userId = participation.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID: " + userId));
        participation.setUser(user);

        // 2. VÉRIFIER SI C'EST UNE PARTICIPATION À UNE ACTIVITÉ
        if (participation.getActivite() != null && participation.getActivite().getIdActivite() != null) {
            Long activiteId = participation.getActivite().getIdActivite();
            Activite activite = activiteRepository.findById(activiteId)
                    .orElseThrow(() -> new RuntimeException("Activité non trouvée avec ID: " + activiteId));
            participation.setActivite(activite);
            participation.setEvent(null);
        }
        // 3. VÉRIFIER SI C'EST UNE PARTICIPATION À UN ÉVÉNEMENT
        else if (participation.getEvent() != null && participation.getEvent().getIdEvent() != null) {
            Long eventId = participation.getEvent().getIdEvent();
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé avec ID: " + eventId));
            participation.setEvent(event);
            participation.setActivite(null);
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
        // Convertir Long en String pour chercher l'utilisateur par ID
        // Note: Si l'ID utilisateur est de type String, il faut adapter
        // Ici on suppose que l'ID utilisateur est passé en String
        return participationRepository.findByUserId(String.valueOf(membreId));
    }

    @Override
    public List<Participation> findByActiviteId(Long activiteId) {
        return participationRepository.findByActiviteId(activiteId);
    }

    @Override
    public List<Participation> findByEventId(Long eventId) {
        return participationRepository.findByEventId(eventId);
    }

    @Override
    public List<Participation> searchParticipations(Long membreId, StatutPresence statutPresence) {
        return participationRepository.searchParticipations(
                membreId != null ? String.valueOf(membreId) : null,
                statutPresence);
    }

    @Override
    public List<Participation> searchAll(Long membreId, Long activiteId, Long eventId,
                                         StatutPresence statutPresence, LocalDate dateStart, LocalDate dateEnd) {
        return participationRepository.searchAll(
                membreId != null ? String.valueOf(membreId) : null,
                activiteId, eventId, statutPresence, dateStart, dateEnd);
    }

    @Override
    public List<Participation> findByMembreIdAndActiviteId(Long membreId, Long activiteId) {
        return participationRepository.findByUserIdAndActiviteId(
                String.valueOf(membreId), activiteId);
    }

    @Override
    public List<Participation> findByMembreIdAndEventId(Long membreId, Long eventId) {
        return participationRepository.findByUserIdAndEventId(
                String.valueOf(membreId), eventId);
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