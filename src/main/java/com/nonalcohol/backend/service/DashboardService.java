package com.nonalcohol.backend.service;

import com.nonalcohol.backend.dto.LabelCountDto;
import com.nonalcohol.backend.dto.MemberRankingDto;
import com.nonalcohol.backend.repository.AttendanceRepository;
import com.nonalcohol.backend.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final MemberRepository memberRepository;       // 회원 저장소
    private final AttendanceRepository attendanceRepository;

    public DashboardService(MemberRepository memberRepository, AttendanceRepository attendanceRepository) {
        this.memberRepository = memberRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<LabelCountDto> getRegionDistribution() {

        List<Object[]> results = memberRepository.countByRegion();
        return results.stream()
                .map(row -> new LabelCountDto((String) row[0], (Long) row[1]))
                .toList();
    }

    public List<MemberRankingDto> getMonthlyRanking() {
        YearMonth thisMonth = YearMonth.now();
        String start = thisMonth.atDay(1).toString();      // "2025-07-01"
        String end = thisMonth.atEndOfMonth().toString();  // "2025-07-31"

        List<Object[]> result = attendanceRepository.countAttendanceNative(start, end);

        return result.stream()
                .map(row -> new MemberRankingDto((String) row[0], ((Number) row[1]).intValue()))
                .sorted(Comparator.comparingInt(MemberRankingDto::getCount).reversed())
                .collect(Collectors.toList());
    }


}
