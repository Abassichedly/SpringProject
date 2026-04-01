package tn.esprit.spring.ARMP.service.interfaces;

import tn.esprit.spring.ARMP.entity.Participation;
import tn.esprit.spring.ARMP.enums.StatutPresence;

import java.time.LocalDate;
import java.util.List;

public interface IParticipationService {

    List<Participation> retrieveAllParticipations();
    Participation addParticipation(Participation participation);
    Participation updateParticipation(Participation participation);
    Participation retrieveParticipation(Long id);
    void removeParticipation(Long id);

    // Recherche simple
    List<Participation> findByStatutPresence(StatutPresence statutPresence);
    List<Participation> findByMembreId(Long membreId);
    List<Participation> findByActiviteId(Long activiteId);
    List<Participation> findByEventId(Long eventId);

    // Recherche multi-critères
    List<Participation> searchParticipations(Long membreId, StatutPresence statutPresence);
    List<Participation> searchAll(Long membreId, Long activiteId, Long eventId,
                                  StatutPresence statutPresence, LocalDate dateStart, LocalDate dateEnd);

    // Spécifique
    List<Participation> findByMembreIdAndActiviteId(Long membreId, Long activiteId);
    List<Participation> findByMembreIdAndEventId(Long membreId, Long eventId);
    long countPresencesByActiviteId(Long activiteId);
    long countPresencesByEventId(Long eventId);
}