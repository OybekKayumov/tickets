package com.ok.tickets.controller;

import com.ok.tickets.domain.dto.ListPublishedEventResponseDto;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.mappers.EventMapper;
import com.ok.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

	private final EventService eventService;
	private final EventMapper eventMapper;

	public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
					@RequestParam(required = false) String q,
					Pageable pageable) {

		Page<Event> events;

		if (q != null && !q.trim().isEmpty()) {

			events= eventService.searchPublishedEvents(q, pageable);
		} else {
			events = eventService.listPublishedEvents(pageable);
		}

		return ResponseEntity.ok(
						events.map(eventMapper::toListPublishedEventResponseDto));
	}

}
