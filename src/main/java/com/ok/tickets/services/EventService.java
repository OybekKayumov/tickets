package com.ok.tickets.services;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {

	Event createEvent(UUID organizerId, CreateEventRequest event) throws UserNotFoundException;

	Page<Event> listEventsForOrganizer(UUID organizerId,  Pageable pageable);
}
