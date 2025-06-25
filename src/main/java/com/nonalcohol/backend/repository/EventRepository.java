package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
