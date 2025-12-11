package com.BookMySeat.tickets.domain;

import com.BookMySeat.tickets.domain.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequest {
    private String name;
    private Double price;
    private Double description;
    private Integer totalAvailable;
}
