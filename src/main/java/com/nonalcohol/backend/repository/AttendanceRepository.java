package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEvent(Event event);
}
