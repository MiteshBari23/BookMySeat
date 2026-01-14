package com.BookMySeat.tickets.services.impl;

import com.BookMySeat.tickets.domain.entities.*;
import com.BookMySeat.tickets.exceptions.QrCodeNotfoundException;
import com.BookMySeat.tickets.exceptions.TicketNotFoundException;
import com.BookMySeat.tickets.repository.QrCodeRepository;
import com.BookMySeat.tickets.repository.TicketRepository;
import com.BookMySeat.tickets.repository.TicketValidationRepository;
import com.BookMySeat.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;
    private final TicketValidationRepository ticketValidationRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotfoundException(
                        String.format("QR Code with the ID %s not found", qrCodeId)
                ));

        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket,TicketValidationMethod.QR_SCAN);
    }

    private @NotNull TicketValidation validateTicket(Ticket ticket,
        TicketValidationMethod ticketValidationMethod){
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethod);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatus);
        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket, TicketValidationMethod.MANUAL);
    }
}
