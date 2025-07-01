package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.dto.AttendanceStatDto;

import java.util.List;

public interface AttendanceRepositoryCustom {
    List<AttendanceStatDto> countAttendanceStatsByMonth(String month);
}
