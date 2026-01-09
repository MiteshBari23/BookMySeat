package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.entities.Ticket;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public interface TicketTypeService {
    Ticket purchaseticket(UUID userId, UUID ticketTypeId) throws IOException, WriterException;
}
