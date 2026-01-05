package com.BookMySeat.tickets.services.impl;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.UpdateEventRequest;
import com.BookMySeat.tickets.domain.UpdateTicketTypeRequest;
import com.BookMySeat.tickets.domain.entities.Event;
import com.BookMySeat.tickets.domain.entities.TicketType;
import com.BookMySeat.tickets.domain.entities.User;
import com.BookMySeat.tickets.exceptions.EventNotFoundException;
import com.BookMySeat.tickets.exceptions.EventUpdateException;
import com.BookMySeat.tickets.exceptions.TicketTypeNotFoundException;
import com.BookMySeat.tickets.exceptions.UserNotFoundException;
import com.BookMySeat.tickets.mappers.EventMapper;
import com.BookMySeat.tickets.repository.EventRepository;
import com.BookMySeat.tickets.repository.UserRepository;
import com.BookMySeat.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));
        Event eventToCreate = new Event();

        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(ticketType -> {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName(ticketType.getName());
            ticketTypeToCreate.setPrice(ticketType.getPrice());
            ticketTypeToCreate.setDescription(ticketType.getDescription());
            ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
            ticketTypeToCreate.setEvent(eventToCreate);
            return ticketTypeToCreate;
        }).toList();

        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID userId, Pageable pageable) {
        return eventRepository.findByOrganizerId(userId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
        if (event.getId() == null) {
            throw new EventUpdateException("Event ID cannot be null");
        }

        if (!id.equals(event.getId())) {
            throw new EventUpdateException("Cannot update an ID of an event ");
        }

        Event existingEvent = eventRepository
                .findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with the ID '%s' does not exist", id))
                );

        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent
                .getTicketTypes()
                .removeIf(existingTicketType ->
                        !requestTicketTypeIds
                                .contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypesIdex = existingEvent
                .getTicketTypes()
                .stream()
                .collect(Collectors
                        .toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketType : event.getTicketTypes()){
            if(ticketType.getId() == null){
                //create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);

            } else if (existingTicketTypesIdex.containsKey(ticketType.getId())) {
                //Update
                TicketType existingTicketType = existingTicketTypesIdex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
            }else {
                throw new TicketTypeNotFoundException(String.format(
                        "Ticket type with ID '%s' not found", ticketType.getId()
                ));
            }
        }
    return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrqanizer(UUID organizerId, UUID id) {
        getEventForOrganizer(organizerId, id).ifPresent(eventRepository::delete);
    }
}
