package com.ok.tickets.mappers;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.CreateTicketTypeRequest;
import com.ok.tickets.domain.dto.CreateEventRequestDto;
import com.ok.tickets.domain.dto.CreateEventResponseDto;
import com.ok.tickets.domain.dto.CreateTicketTypeRequestDto;
import com.ok.tickets.domain.enteties.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "string", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

	CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

	CreateEventRequest fromDto(CreateEventRequestDto dto);

	CreateEventResponseDto toDto(Event event);
}
