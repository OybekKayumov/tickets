package com.ok.tickets.services.impl;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.TicketType;
import com.ok.tickets.domain.enteties.User;
import com.ok.tickets.exceptions.UserNotFoundException;
import com.ok.tickets.repos.EventRepo;
import com.ok.tickets.repos.UserRepo;
import com.ok.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final UserRepo userRepo;
	private final EventRepo eventRepo;

	@Override
	public Event createEvent(UUID organizerId, CreateEventRequest event) throws UserNotFoundException {

		User organizer = userRepo.findById(organizerId)
						.orElseThrow(() -> new UserNotFoundException(
						    String.format("User with ID '%s' not found", organizerId)
		));

		Event eventToCreate = new Event();

		List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
						ticketType -> {
							TicketType ticketTypeToCreate = new TicketType();
							ticketTypeToCreate.setName(ticketType.getName());
							ticketTypeToCreate.setPrice(ticketType.getPrice());
							ticketTypeToCreate.setDescription(ticketType.getDescription());
							ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
							ticketTypeToCreate.setEvent(eventToCreate);
							return ticketTypeToCreate;
						}).toList();

		eventToCreate.setName(event.getName());
		eventToCreate.setStart(event.getStart());
		eventToCreate.setEnd(event.getEnd());
		eventToCreate.setVenue(event.getVenue());
		eventToCreate.setSalesStart(event.getSalesStart());
		eventToCreate.setSalesEnd(event.getSalesEnd());
		eventToCreate.setStatus(event.getStatus());
		eventToCreate.setOrganizer(organizer);
		eventToCreate.setTicketTypes(ticketTypesToCreate);

		return eventRepo.save(eventToCreate);

	}

	@Override
	public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {

		return eventRepo.findByOrganizerId(organizerId, pageable);

	}
}
