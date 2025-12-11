package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
