package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Event;

import java.util.List;

public interface IEventService {
    List<Event> retrieveAllEvents();
    Event addEvent(Event event);
    Event updateEvent(Event event);
    Event retrieveEvent(Long id);
    void removeEvent(Long id);
}