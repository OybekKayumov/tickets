package com.ok.tickets.repos;

import com.ok.tickets.domain.enteties.Event;
import com.ok.tickets.domain.enteties.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {

	Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

	Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

	Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);

	@Query(value = "select * from events where " +
					"status = 'PUBLISHED' and " +
					"to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
					"@@ plainto_tsquery('english', :searchTerm)",
					countQuery = "select count(*) from events where " +
									"status = 'PUBLISHED' and " +
									"to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, ''))" +
									"@@ plainto_tsquery('english', :searchTerm)",
					nativeQuery =	true)
	Page<Event> searchEvents(@Param("searchTerm") String searchTerm, Pageable pageable);

}
