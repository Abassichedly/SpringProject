package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

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
        List<Participation> participations = participationRepository.findAll();

        // Load all related data
        for (Participation p : participations) {
            loadRelatedData(p);
        }

        return participations;
    }

    @Override
    @Transactional
    public Participation addParticipation(Participation participation) {
        // Extract and set user ID
        if (participation.getUser() != null && participation.getUser().getId() != null) {
            String userId = participation.getUser().getId();
            participation.setUserId(userId);
        }

        if (participation.getUserId() == null) {
            throw new RuntimeException("L'utilisateur est requis");
        }

        // Handle activity
        if (participation.getActivite() != null && participation.getActivite().getIdActivite() != null) {
            Long activiteId = participation.getActivite().getIdActivite();
            Activite activite = activiteRepository.findById(activiteId).orElse(null);
            participation.setActivite(activite);
            participation.setEvent(null);
        }
        // Handle event
        else if (participation.getEvent() != null && participation.getEvent().getIdEvent() != null) {
            Long eventId = participation.getEvent().getIdEvent();
            Event event = eventRepository.findById(eventId).orElse(null);
            participation.setEvent(event);
            participation.setActivite(null);
        }

        // Set defaults
        if (participation.getDateInscription() == null) {
            participation.setDateInscription(LocalDate.now());
        }
        if (participation.getStatutPresence() == null) {
            participation.setStatutPresence(StatutPresence.INSCRIT);
        }
        if (participation.getRole() == null) {
            participation.setRole("PARTICIPANT");
        }

        Participation saved = participationRepository.save(participation);
        loadRelatedData(saved);

        return saved;
    }

    @Override
    public Participation updateParticipation(Participation participation) {
        Participation updated = participationRepository.save(participation);
        loadRelatedData(updated);
        return updated;
    }

    @Override
    public Participation retrieveParticipation(Long id) {
        Participation p = participationRepository.findById(id).orElse(null);
        if (p != null) {
            loadRelatedData(p);
        }
        return p;
    }

    @Override
    public void removeParticipation(Long id) {
        participationRepository.deleteById(id);
    }

    private void loadRelatedData(Participation p) {
        // Load user
        if (p.getUserId() != null) {
            User user = userRepository.findById(p.getUserId()).orElse(null);
            p.setUser(user);
        }

        // Load activity
        if (p.getActivite() != null && p.getActivite().getIdActivite() != null) {
            Activite activite = activiteRepository.findById(p.getActivite().getIdActivite()).orElse(null);
            p.setActivite(activite);
        }

        // Load event
        if (p.getEvent() != null && p.getEvent().getIdEvent() != null) {
            Event event = eventRepository.findById(p.getEvent().getIdEvent()).orElse(null);
            p.setEvent(event);
        }
    }

    @Override
    public List<Participation> findByStatutPresence(StatutPresence statutPresence) {
        return participationRepository.findByStatutPresence(statutPresence);
    }

    @Override
    public List<Participation> findByMembreId(Long membreId) {
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