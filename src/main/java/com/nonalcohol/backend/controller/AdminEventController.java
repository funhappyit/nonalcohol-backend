package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.EventWithMembersDto;
import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Event;
import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.AttendanceRepository;
import com.nonalcohol.backend.repository.EventRepository;
import com.nonalcohol.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 🔧 AdminEventController.java
// 관리자용 벙(모임) 등록 및 조회 기능을 담당하는 컨트롤러

@RestController
@RequestMapping("/api/admin/events") // 모든 경로가 "/api/admin/events"로 시작
@RequiredArgsConstructor // 생성자 주입 자동 생성 (final 필드만)
public class AdminEventController {

    private final EventRepository eventRepository;         // 벙 저장소
    private final MemberRepository memberRepository;       // 회원 저장소
    private final AttendanceRepository attendanceRepository; // 출석 저장소

    // ✅ 벙 단독 생성 (참가자 없이)
    // POST /api/admin/events
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        // 전달받은 event를 저장하고 반환
        Event saved = eventRepository.save(event);
        return ResponseEntity.ok(saved);
    }

    // ✅ 벙 + 참가자 동시 등록
    // POST /api/admin/events/with-members
    @PostMapping("/with-members")
    public ResponseEntity<?> createEventWithMembers(@RequestBody EventWithMembersDto dto) {
        // 1. 벙 객체 생성 및 저장
        Event event = Event.builder()
                .title(dto.getTitle())
                .location(dto.getLocation())
                .date(dto.getDate())
                .build();
        Event saved = eventRepository.save(event);

        // 2. 참가자 ID를 기반으로 출석 정보 저장
        for (Long memberId : dto.getMemberIds()) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원 없음"));
            Attendance attendance = Attendance.builder()
                    .event(saved)
                    .member(member)
                    .status("참석") // 상태는 기본 "참석"
                    .build();
            attendanceRepository.save(attendance);
        }

        return ResponseEntity.ok(saved);
    }

    // ✅ 모든 벙 조회 (참석자 아이디 포함)
    // GET /api/admin/events
    @GetMapping
    public List<EventWithMembersDto> getEventsWithUsernames() {
        List<Event> events = eventRepository.findAll(); // 모든 벙 조회
        List<EventWithMembersDto> result = new ArrayList<>();

        for (Event event : events) {
            EventWithMembersDto dto = new EventWithMembersDto();
            dto.setTitle(event.getTitle());
            dto.setLocation(event.getLocation());
            dto.setDate(event.getDate());

            // 해당 벙의 출석자 목록 조회 → username만 추출
            List<Attendance> attendances = attendanceRepository.findByEvent(event);
            List<String> usernames = attendances.stream()
                    .map(a -> a.getMember().getUsername())
                    .collect(Collectors.toList());

            dto.setUsernames(usernames); // username 리스트 담기
            result.add(dto);
        }

        return result;
    }
}

