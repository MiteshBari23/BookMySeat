package com.BookMySeat.tickets.controllers;

import com.BookMySeat.tickets.domain.dtos.ListEventResponseDto;
import com.BookMySeat.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.BookMySeat.tickets.mappers.EventMapper;
import com.BookMySeat.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvent(Pageable pageable){
        return ResponseEntity.ok(eventService
                .listPublishedEvent(pageable)
                .map(eventMapper::toListPublishedEventResponseDto));
    }

}
