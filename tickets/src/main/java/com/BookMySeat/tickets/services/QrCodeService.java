package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.entities.QrCode;
import com.BookMySeat.tickets.domain.entities.Ticket;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket) throws WriterException, IOException;

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
