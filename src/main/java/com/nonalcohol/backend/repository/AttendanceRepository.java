package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEvent(Event event);

    // AttendanceRepository에 아래 쿼리 메서드 추가
    @Query("SELECT new com.nonalcohol.backend.dto.AttendanceStatDto(m.id, m.name, m.username, COUNT(a)) " +
            "FROM Attendance a " +
            "JOIN a.member m " +
            "WHERE a.status = '참석' " +
            "GROUP BY m.id, m.name, m.username")
    List<AttendanceStatDto> countAttendanceStats();

    @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE a.event = :event")
    List<Attendance> findByEventWithMember(@Param("event") Event event);


}
