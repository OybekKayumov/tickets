package com.ok.tickets.domain;

import com.ok.tickets.domain.enteties.EventStatusEnum;
import com.ok.tickets.domain.enteties.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

	private String name;
	private LocalDateTime start;
	private LocalDateTime end;
	private String venue;
	private LocalDateTime salesStart;
	private LocalDateTime salesEnd;

	private EventStatusEnum status;

//	private User organizer;

	private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();

}
