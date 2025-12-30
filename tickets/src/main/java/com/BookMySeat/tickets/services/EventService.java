package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.dtos.EventSummaryDto;
import com.BookMySeat.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    // 🔥 ADD THIS METHOD
    Page<EventSummaryDto> listEvents (UUID userId, Pageable pageable);
}
