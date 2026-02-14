package com.ok.tickets.repos;

import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {

	Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

	Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

	Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);
}
