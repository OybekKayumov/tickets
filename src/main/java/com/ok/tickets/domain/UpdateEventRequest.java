package com.ok.tickets.domain;

import com.ok.tickets.domain.enteties.EventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {

	private UUID id;

	private String name;
	private LocalDateTime start;
	private LocalDateTime end;
	private String venue;
	private LocalDateTime salesStart;
	private LocalDateTime salesEnd;

	private EventStatusEnum status;

//	private User organizer;

	private List<UpdateTicketTypeRequest> ticketTypes = new ArrayList<>();

}
