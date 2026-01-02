package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
    Page<Event> listEventsForOrganizer(UUID userId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);
}
