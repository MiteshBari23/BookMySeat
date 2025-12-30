package com.BookMySeat.tickets.mappers;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.CreateTicketTypeRequest;
import com.BookMySeat.tickets.domain.dtos.CreateEventRequestDto;
import com.BookMySeat.tickets.domain.dtos.CreateEventResponseDto;
import com.BookMySeat.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.BookMySeat.tickets.domain.dtos.EventSummaryDto;
import com.BookMySeat.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    EventSummaryDto toSummaryDto(Event event);
}
