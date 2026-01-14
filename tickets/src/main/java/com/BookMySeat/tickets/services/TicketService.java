package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    Page<Ticket> listTicketForUser(UUID userId, Pageable pageable);
    Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId);
}
