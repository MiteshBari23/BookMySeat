package com.BookMySeat.tickets.services;

import com.BookMySeat.tickets.domain.entities.QrCode;
import com.BookMySeat.tickets.domain.entities.Ticket;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeService {

    QrCode generaterQrCode(Ticket ticket) throws WriterException, IOException;

}
