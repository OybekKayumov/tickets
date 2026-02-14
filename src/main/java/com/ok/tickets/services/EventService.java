package com.ok.tickets.services;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.UpdateEventRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.exceptions.EventNotFoundException;
import com.ok.tickets.exceptions.EventUpdateException;
import com.ok.tickets.exceptions.TicketTypeNotFoundException;
import com.ok.tickets.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

	Event createEvent(UUID organizerId, CreateEventRequest event) throws UserNotFoundException;

	Page<Event> listEventsForOrganizer(UUID organizerId,  Pageable pageable);

	Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);

	Event updateEventForOrganizer(UUID organizerId, UUID id,
	                        UpdateEventRequest event) throws EventUpdateException, EventNotFoundException, TicketTypeNotFoundException;

	void deleteEventForOrganizer(UUID organizerId, UUID id);

	Page<Event> listPublishedEvents(Pageable pageable);
}
