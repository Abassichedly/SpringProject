package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Event;
import tn.purebillion.spring.demo.enums.StatutEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {
    List<Event> retrieveAllEvents();
    Event addEvent(Event event);
    Event updateEvent(Event event);
    Event retrieveEvent(Long id);
    void removeEvent(Long id);

    List<Event> findByNomContaining(String nom);
    List<Event> findByLieuContaining(String lieu);
    List<Event> findByStatut(StatutEvent statut);
    List<Event> findByClubId(Long clubId);
    List<Event> findByPrixAdherentLessThan(Double prix);
    List<Event> searchEvents(String nom, String lieu, StatutEvent statut);
    List<Event> searchAll(String nom, String lieu, StatutEvent statut, Long clubId,
                          LocalDateTime dateStart, LocalDateTime dateEnd, Double prixMax);
    List<Event> findAllOrderByDateDebutAsc();
}