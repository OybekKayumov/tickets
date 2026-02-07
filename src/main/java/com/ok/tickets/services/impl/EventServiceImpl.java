package com.ok.tickets.services.impl;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.TicketType;
import com.ok.tickets.domain.enteties.User;
import com.ok.tickets.exceptions.UserNotFoundException;
import com.ok.tickets.repos.UserRepo;
import com.ok.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final UserRepo userRepo;

	@Override
	public Event createEvent(UUID organizerId, CreateEventRequest event) throws UserNotFoundException {

		User organizer = userRepo.findById(organizerId)
						.orElseThrow(() -> new UserNotFoundException(
						    String.format("User with ID '%s' not found", organizerId)
		));


		List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
						ticketType -> {
							TicketType ticketTypeToCreate = new TicketType();
							ticketTypeToCreate.setName(ticketType.getName());
							ticketTypeToCreate.setPrice(ticketType.getPrice());
							ticketTypeToCreate.setDescription(ticketType.getDescription());
							ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
							return ticketTypeToCreate;
						}).toList();

		Event eventToCreate = new Event();
		eventToCreate.setName(event.getName());
		eventToCreate.setStart(event.getStart());
		eventToCreate.setEnd(event.getEnd());
		eventToCreate.setVenue(event.getVenue());
		eventToCreate.setSalesStart(event.getSalesStart());
		eventToCreate.setSalesEnd(event.getSalesEnd());
		eventToCreate.setStatus(event.getStatus());
		eventToCreate.setOrganizer(organizer);
		eventToCreate.setTicketTypes(ticketTypesToCreate);

		return null;
	}
}
