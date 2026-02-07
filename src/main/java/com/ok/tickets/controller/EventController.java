package com.ok.tickets.controller;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.dto.CreateEventRequestDto;
import com.ok.tickets.domain.dto.CreateEventResponseDto;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.exceptions.UserNotFoundException;
import com.ok.tickets.mappers.EventMapper;
import com.ok.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(params = "/api/v1/enevts")
@RequiredArgsConstructor
public class EventController {

	private final EventMapper eventMapper;
	private final EventService eventService;

	@PostMapping
	public ResponseEntity<CreateEventResponseDto> createEvent(
					@AuthenticationPrincipal Jwt jwt,
					@Valid @RequestBody CreateEventRequestDto createEventRequestDto) throws UserNotFoundException {

		CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
		UUID userId = UUID.fromString(jwt.getSubject());

		Event createdEvent = eventService.createEvent(userId, createEventRequest);
		CreateEventResponseDto createEventResponseDto =	eventMapper.toDto(createdEvent);

		return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

	}
}
