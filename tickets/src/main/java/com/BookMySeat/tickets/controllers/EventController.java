package com.BookMySeat.tickets.controllers;

import com.BookMySeat.tickets.domain.CreateEventRequest;
import com.BookMySeat.tickets.domain.dtos.CreateEventRequestDto;
import com.BookMySeat.tickets.domain.dtos.CreateEventResponseDto;
import com.BookMySeat.tickets.domain.dtos.EventSummaryDto;
import com.BookMySeat.tickets.domain.entities.Event;
import com.BookMySeat.tickets.mappers.EventMapper;
import com.BookMySeat.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<@NotNull CreateEventResponseDto> createEvent(
            @org.jetbrains.annotations.NotNull @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto){
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId = UUID.fromString(jwt.getSubject());

        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<EventSummaryDto> listEvents(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10)Pageable pageable
            ){
        UUID userId = UUID.fromString(jwt.getSubject());
        return eventService.listEvents(userId, pageable);
    }
}


