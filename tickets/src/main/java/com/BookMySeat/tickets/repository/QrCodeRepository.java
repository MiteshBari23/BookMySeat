package com.BookMySeat.tickets.repository;

import com.BookMySeat.tickets.domain.entities.QrCode;
import com.BookMySeat.tickets.domain.entities.QrCodeStatusEnum;
import com.BookMySeat.tickets.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID tickedId, UUID ticketPurchaserId);
    Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);
}
