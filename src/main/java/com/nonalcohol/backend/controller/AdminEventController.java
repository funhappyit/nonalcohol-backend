package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.EventWithMembersDto;
import com.nonalcohol.backend.entity.Attendance;
import com.nonalcohol.backend.entity.Event;
import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.AttendanceRepository;
import com.nonalcohol.backend.repository.AttendanceRepositoryCustom;
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
    private final AttendanceRepositoryCustom attendanceRepositoryCustom;

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
        List<Event> events = eventRepository.findAll();
        List<EventWithMembersDto> result = new ArrayList<>();

        for (Event event : events) {
            EventWithMembersDto dto = new EventWithMembersDto();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setLocation(event.getLocation());
            dto.setDate(event.getDate());

            List<Attendance> attendances = attendanceRepositoryCustom.findByEventWithMember(event);


            List<String> memberNames = new ArrayList<>();
            List<Long> memberIds = new ArrayList<>();

            for (Attendance a : attendances) {
                memberNames.add(a.getMember().getName()); // ✅ 이름으로
                memberIds.add(a.getMember().getId()); // ✅ 추가
            }

            dto.setMemberNames(memberNames);
            dto.setMemberIds(memberIds); // ✅ 추가

            result.add(dto);
        }

        return result;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEventWithMembers(@PathVariable Long id, @RequestBody EventWithMembersDto dto) {
        return eventRepository.findById(id).map(event -> {
            // 🔁 이벤트 정보 수정
            event.setTitle(dto.getTitle());
            event.setLocation(dto.getLocation());
            event.setDate(dto.getDate());
            eventRepository.save(event);

            // 🔁 기존 참석 기록 삭제
            List<Attendance> oldAttendances = attendanceRepositoryCustom.findByEvent(event);
            attendanceRepository.deleteAll(oldAttendances);

            // 🔁 새로운 참석자 등록
            for (Long memberId : dto.getMemberIds()) {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("회원 없음"));
                Attendance newAttendance = Attendance.builder()
                        .event(event)
                        .member(member)
                        .status("참석")
                        .build();
                attendanceRepository.save(newAttendance);
            }

            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }


    // ✅ 벙 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id).map(event -> {
            // 관련 참석 기록 먼저 삭제
            List<Attendance> attendances = attendanceRepositoryCustom.findByEvent(event);
            attendanceRepository.deleteAll(attendances);

            // 이벤트 삭제
            eventRepository.delete(event);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }




}

