package com.ok.tickets.controller;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.dto.CreateEventRequestDto;
import com.ok.tickets.domain.dto.CreateEventResponseDto;
import com.ok.tickets.domain.dto.GetEventDetailsResponseDto;
import com.ok.tickets.domain.dto.ListEventResponseDto;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.exceptions.UserNotFoundException;
import com.ok.tickets.mappers.EventMapper;
import com.ok.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/enevts")
@RequiredArgsConstructor
public class EventController {

	private final EventMapper eventMapper;
	private final EventService eventService;

	@PostMapping
	public ResponseEntity<CreateEventResponseDto> createEvent(
					@AuthenticationPrincipal Jwt jwt,
					@Valid @RequestBody CreateEventRequestDto createEventRequestDto) throws UserNotFoundException {

		CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);

		//UUID userId = UUID.fromString(jwt.getSubject());
		UUID userId = parseUserId(jwt);

		Event createdEvent = eventService.createEvent(userId, createEventRequest);
		CreateEventResponseDto createEventResponseDto =	eventMapper.toDto(createdEvent);

		return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

	}

	@GetMapping
	public ResponseEntity<Page<ListEventResponseDto>> listEvents(
					@AuthenticationPrincipal Jwt jwt,
					Pageable pageable) {

		UUID userId = parseUserId(jwt);
		Page<Event> events = eventService.listEventsForOrganizer(userId, pageable);

		return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));

	}

	@GetMapping(path = "/{eventId}")
	public ResponseEntity<GetEventDetailsResponseDto> getEvent(
					@AuthenticationPrincipal Jwt jwt,
					@PathVariable UUID eventId) {

		UUID userId = parseUserId(jwt);

		return eventService.getEventForOrganizer(userId, eventId)
						.map(eventMapper::toGetEventDetailsResponseDto)
						.map(ResponseEntity::ok)
						.orElse(ResponseEntity.notFound().build());

	}

	private UUID parseUserId(Jwt jwt) {
		return UUID.fromString(jwt.getSubject());
	}
}
