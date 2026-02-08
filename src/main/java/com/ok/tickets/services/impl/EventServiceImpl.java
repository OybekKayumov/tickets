package com.ok.tickets.services.impl;

import com.ok.tickets.domain.CreateEventRequest;
import com.ok.tickets.domain.UpdateEventRequest;
import com.ok.tickets.domain.UpdateTicketTypeRequest;
import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.TicketType;
import com.ok.tickets.domain.enteties.User;
import com.ok.tickets.exceptions.EventNotFoundException;
import com.ok.tickets.exceptions.EventUpdateException;
import com.ok.tickets.exceptions.TicketTypeNotFoundException;
import com.ok.tickets.exceptions.UserNotFoundException;
import com.ok.tickets.repos.EventRepo;
import com.ok.tickets.repos.UserRepo;
import com.ok.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final UserRepo userRepo;
	private final EventRepo eventRepo;

	@Override
	@Transactional
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

	@Override
	public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
		return eventRepo.findByIdAndOrganizerId(id, organizerId);
	}

	@Override
	@Transactional
	public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) throws EventUpdateException, EventNotFoundException, TicketTypeNotFoundException {

		if (event.getId() == null) {
			throw new EventUpdateException("Event IDs cannot be null");
		}

		if (!id.equals(event.getId())) {
			throw new EventUpdateException("Provided IDs don't match");
		}

		Event existingEvent = eventRepo.findByIdAndOrganizerId(id, organizerId)
						.orElseThrow(() -> new EventNotFoundException(
										String.format("Event with ID '%s' does not exist", id)));

		existingEvent.setName(event.getName());
		existingEvent.setStart(event.getStart());
		existingEvent.setEnd(event.getEnd());
		existingEvent.setVenue(event.getVenue());
		existingEvent.setSalesStart(event.getSalesStart());
		existingEvent.setSalesEnd(event.getSalesEnd());
		existingEvent.setStatus(event.getStatus());

		Set<UUID> requestTicketTypeId = event.getTicketTypes()
						.stream()
						.map(UpdateTicketTypeRequest::getId)
						.filter(Objects::nonNull)
						.collect(Collectors.toSet());

		existingEvent.getTicketTypes().removeIf(existingTicketType ->
										!requestTicketTypeId.contains(existingTicketType.getId())
		);

		Map<UUID, TicketType> existingTicketTypesIndex = existingEvent
										.getTicketTypes()
										.stream()
										.collect(Collectors.toMap(
																TicketType::getId,
																Function.identity()));

		for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {

			if (null == ticketType.getId()) {

				TicketType ticketTypeToCreate = new TicketType();
				ticketTypeToCreate.setName(ticketType.getName());
				ticketTypeToCreate.setPrice(ticketType.getPrice());
				ticketTypeToCreate.setDescription(ticketType.getDescription());
				ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
				ticketTypeToCreate.setEvent(existingEvent);

				existingEvent.getTicketTypes().add(ticketTypeToCreate);

			} else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {

				TicketType existingTicketType =
								existingTicketTypesIndex.get(ticketType.getId());
				existingTicketType.setName(ticketType.getName());
				existingTicketType.setPrice(ticketType.getPrice());
				existingTicketType.setDescription(ticketType.getDescription());
				existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());

			} else {
				throw new TicketTypeNotFoundException(String.format("Ticket type with ID '%s' not found", ticketType.getId()));
			}
		}

		return eventRepo.save(existingEvent);

	}
}
