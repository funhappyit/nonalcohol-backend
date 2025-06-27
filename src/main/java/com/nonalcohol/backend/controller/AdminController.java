package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.dto.MemberDto;
import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.AttendanceRepository;
import com.nonalcohol.backend.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// 🔧 AdminController.java
// 관리자용 회원 조회 및 삭제 기능을 제공하는 REST 컨트롤러

@RestController // JSON 형태의 HTTP 응답을 반환하는 컨트롤러
@RequestMapping("/api/admin") // 이 컨트롤러의 모든 경로는 "/api/admin"으로 시작됨
public class AdminController {

    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository; // ✅ 추가
    // 💡 생성자 주입 방식으로 Repository 사용
    public AdminController(MemberRepository memberRepository, AttendanceRepository attendanceRepository) {
        this.memberRepository = memberRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // ✅ 전체 회원 목록 조회 API (GET /api/admin/members)
    @GetMapping("/members")
    public Page<MemberDto> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::from); // Entity → DTO 변환
    }

    // ✅ 회원 삭제 API (DELETE /api/admin/members/{id})
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        // 해당 ID의 회원을 삭제
        memberRepository.deleteById(id);
        // 200 OK 응답만 반환 (내용 없음)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attendance/stats")
    public List<AttendanceStatDto> getAttendanceStats() {
        List<AttendanceStatDto> stats = attendanceRepository.countAttendanceStats();

        // 👇 전체 회원 조회 후 참석 기록이 없는 회원 추가
        Set<Long> attendedIds = stats.stream()
                .map(AttendanceStatDto::getId)
                .collect(Collectors.toSet());

        List<AttendanceStatDto> nonAttendees = memberRepository.findAll().stream()
                .filter(m -> !attendedIds.contains(m.getId()))
                .map(m -> new AttendanceStatDto(m.getId(), m.getName(), m.getUsername(), 0L))
                .toList();

        stats.addAll(nonAttendees);

        return stats.stream()
                .sorted(Comparator.comparingLong(AttendanceStatDto::getCount).reversed())
                .toList();
    }

}
