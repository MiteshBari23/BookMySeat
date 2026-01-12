package com.BookMySeat.tickets.mappers;

import com.BookMySeat.tickets.domain.dtos.ListTicketResponseDto;
import com.BookMySeat.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.BookMySeat.tickets.domain.entities.Ticket;
import com.BookMySeat.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
