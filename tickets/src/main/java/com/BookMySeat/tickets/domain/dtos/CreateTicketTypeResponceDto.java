package com.BookMySeat.tickets.domain.dtos;

import com.BookMySeat.tickets.domain.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeResponceDto {
    private UUID id;
    private String name;
    private Double price;
    private Double description;
    private Integer totalAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
