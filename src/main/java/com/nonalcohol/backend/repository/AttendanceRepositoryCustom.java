package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.dto.WeeklyParticipationDto;
import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Event;

import java.util.List;

public interface AttendanceRepositoryCustom {
    List<AttendanceStatDto> countAttendanceStatsByMonth(String month);
    List<Attendance> findByEvent(Event event);
    List<Attendance> findByEventWithMember(Event event);
    List<Object[]> countAttendanceNative(String start, String end); // ✅ 추가
    List<WeeklyParticipationDto> getWeeklyParticipation();

}
