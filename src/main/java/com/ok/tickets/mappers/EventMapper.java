package com.ok.tickets.mappers;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.CreateTicketTypeRequest;
import com.ok.tickets.domain.UpdateEventRequest;
import com.ok.tickets.domain.UpdateTicketTypeRequest;
import com.ok.tickets.domain.dto.*;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy =
				ReportingPolicy.IGNORE)
public interface EventMapper {

	CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

	CreateEventRequest fromDto(CreateEventRequestDto dto);

	CreateEventResponseDto toDto(Event event);

	ListEventTicketTypeResponseDto toDto(TicketType ticketType);

	ListEventResponseDto toListEventResponseDto(Event event);

	GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

	GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

	UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

	UpdateEventRequest fromDto(UpdateEventRequestDto dto);

	UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

	UpdateEventResponseDto toUpdateEventResponseDto(Event event);
}
