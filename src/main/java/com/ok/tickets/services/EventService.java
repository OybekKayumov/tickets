package com.ok.tickets.services;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.exceptions.UserNotFoundException;

import java.util.UUID;

public interface EventService {

	Event createEvent(UUID organizerId, CreateEventRequest event) throws UserNotFoundException;
}
