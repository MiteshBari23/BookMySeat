package com.BookMySeat.tickets.controllers;

import com.BookMySeat.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.BookMySeat.tickets.domain.dtos.ListEventResponseDto;
import com.BookMySeat.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.BookMySeat.tickets.domain.entities.Event;
import com.BookMySeat.tickets.mappers.EventMapper;
import com.BookMySeat.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvent(
            @RequestParam(required = false) String q,
            Pageable pageable){

            Page<Event> events;
            if(q != null && !q.trim().isEmpty()){
                events = eventService.searchPublishedEvents(q, pageable);
            }else {
                events = eventService.listPublishedEvent(pageable);
            }

        return ResponseEntity.ok(
                events.map(eventMapper::toListPublishedEventResponseDto));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId
    ){
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
