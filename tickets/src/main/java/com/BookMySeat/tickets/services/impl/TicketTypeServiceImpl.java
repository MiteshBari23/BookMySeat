package com.BookMySeat.tickets.services.impl;

import com.BookMySeat.tickets.domain.entities.Ticket;
import com.BookMySeat.tickets.domain.entities.TicketStatusEnum;
import com.BookMySeat.tickets.domain.entities.TicketType;
import com.BookMySeat.tickets.domain.entities.User;
import com.BookMySeat.tickets.exceptions.TicketSoldOutException;
import com.BookMySeat.tickets.exceptions.TicketTypeNotFoundException;
import com.BookMySeat.tickets.exceptions.UserNotFoundException;
import com.BookMySeat.tickets.repository.TicketRepository;
import com.BookMySeat.tickets.repository.TicketTypeRepository;
import com.BookMySeat.tickets.repository.UserRepository;
import com.BookMySeat.tickets.services.QrCodeService;
import com.BookMySeat.tickets.services.TicketTypeService;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseticket(UUID userId, UUID ticketTypeId) throws IOException, WriterException {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with the ID %s not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(() -> new TicketTypeNotFoundException(
                String.format("Ticket with ticket type ID %s not found", ticketTypeId)
        ));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());

        Integer totalAvailable = ticketType.getTotalAvailable();

        if(purchasedTickets + 1 > totalAvailable){
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);

    }
}
