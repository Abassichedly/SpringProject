package tn.purebillion.spring.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.purebillion.spring.demo.entity.Event;
import tn.purebillion.spring.demo.service.interfaces.IEventService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("event")
public class EventController {

    IEventService eventService;

    @GetMapping("/listEvent")
    public List<Event> retrieveAllEvents() {
        return eventService.retrieveAllEvents();
    }

    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    @PutMapping("/update")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @GetMapping("/getbyid/{idEvent}")
    public Event retrieveEvent(@PathVariable Long idEvent) {
        return eventService.retrieveEvent(idEvent);
    }

    @DeleteMapping("/delete/{idEvent}")
    public void removeEvent(@PathVariable Long idEvent) {
        eventService.removeEvent(idEvent);
    }
}