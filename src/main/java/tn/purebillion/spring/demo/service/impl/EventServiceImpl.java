package tn.purebillion.spring.demo.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.purebillion.spring.demo.entity.Event;
import tn.purebillion.spring.demo.enums.StatutEvent;
import tn.purebillion.spring.demo.repository.EventRepository;
import tn.purebillion.spring.demo.service.interfaces.IEventService;

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
}