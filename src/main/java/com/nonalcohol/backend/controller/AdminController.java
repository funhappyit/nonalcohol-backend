package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.dto.MemberDto;
import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.AttendanceRepository;
import com.nonalcohol.backend.repository.AttendanceRepositoryCustom;
import com.nonalcohol.backend.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 🔧 AdminController.java
// 관리자용 회원 조회 및 삭제 기능을 제공하는 REST 컨트롤러

@RestController // JSON 형태의 HTTP 응답을 반환하는 컨트롤러
@RequestMapping("/api/admin") // 이 컨트롤러의 모든 경로는 "/api/admin"으로 시작됨
public class AdminController {

    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository; // ✅ 추가
    private final AttendanceRepositoryCustom attendanceRepositoryCustom;
    // 💡 생성자 주입 방식으로 Repository 사용
    public AdminController(MemberRepository memberRepository, AttendanceRepository attendanceRepository, AttendanceRepositoryCustom attendanceRepositoryCustom) {
        this.memberRepository = memberRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceRepositoryCustom = attendanceRepositoryCustom;
    }

    // ✅ 전체 회원 목록 조회 API (GET /api/admin/members)
    @GetMapping("/members")
    public Page<MemberDto> getAllMembers(@RequestParam(required = false) String keyword,
                                         Pageable pageable) {
        Page<Member> page;

        if (keyword != null && !keyword.isBlank()) {
            page = memberRepository.searchByKeyword(keyword, pageable);
        } else {
            Pageable sorted = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Order.asc("role"), Sort.Order.asc("name"))
            );
            page = memberRepository.findAll(sorted);
        }

        return page.map(MemberDto::from);
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
    public List<AttendanceStatDto> getStatsForMonth(@RequestParam("month") int month) {
        String monthStr = String.format("%02d", month); // 6 → "06"
        return attendanceRepositoryCustom.countAttendanceStatsByMonth(monthStr);
    }


}
