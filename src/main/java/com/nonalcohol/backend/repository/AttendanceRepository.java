package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEvent(Event event);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE a.event = :event")
    List<Attendance> findByEventWithMember(@Param("event") Event event);


    @Query(value = """
    SELECT m.name, COUNT(*) 
    FROM attendance a 
    JOIN member m ON a.member_id = m.id 
    JOIN event e ON a.event_id = e.id 
    WHERE a.status = '참석'
      AND e.date BETWEEN :start AND :end
    GROUP BY m.name
    """, nativeQuery = true)
    List<Object[]> countAttendanceNative(@Param("start") String start,
                                         @Param("end") String end);



}
