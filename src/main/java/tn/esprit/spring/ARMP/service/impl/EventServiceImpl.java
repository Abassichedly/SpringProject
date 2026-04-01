package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.Event;
import tn.esprit.spring.ARMP.enums.StatutEvent;
import tn.esprit.spring.ARMP.repository.EventRepository;
import tn.esprit.spring.ARMP.service.interfaces.IEventService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    EventRepository eventRepository;

    @Override
    public List<Event> retrieveAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event addEvent(Event event) {
        event.setStatut(StatutEvent.EN_PREPARATION);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event retrieveEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void removeEvent(Long id) {
        eventRepository.deleteById(id);
    }
    @Override
    public List<Event> findByNomContaining(String nom) {
        return eventRepository.findByNomContaining(nom);
    }

    @Override
    public List<Event> findByLieuContaining(String lieu) {
        return eventRepository.findByLieuContaining(lieu);
    }

    @Override
    public List<Event> findByStatut(StatutEvent statut) {
        return eventRepository.findByStatut(statut);
    }

    @Override
    public List<Event> findByClubId(Long clubId) {
        return eventRepository.findByClubIdClub(clubId);
    }

    @Override
    public List<Event> findByPrixAdherentLessThan(Double prix) {
        return eventRepository.findByPrixAdherentLessThan(prix);
    }

    @Override
    public List<Event> searchEvents(String nom, String lieu, StatutEvent statut) {
        return eventRepository.searchEvents(nom, lieu, statut);
    }

    @Override
    public List<Event> searchAll(String nom, String lieu, StatutEvent statut, Long clubId,
                                 LocalDateTime dateStart, LocalDateTime dateEnd, Double prixMax) {
        return eventRepository.searchAll(nom, lieu, statut, clubId, dateStart, dateEnd, prixMax);
    }

    @Override
    public List<Event> findAllOrderByDateDebutAsc() {
        return eventRepository.findAllByOrderByDateDebutAsc();
    }
}