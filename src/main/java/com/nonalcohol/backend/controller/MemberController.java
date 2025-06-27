package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.MemberDto;
import com.nonalcohol.backend.dto.SimpleMemberRegisterDto;
import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/members") // 모든 경로는 "/api/members"로 시작
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 생성
public class MemberController {

    private final MemberRepository memberRepository;

    // ✅ 회원 등록 (POST /api/members)
    @PostMapping
    public ResponseEntity<Member> register(@RequestBody Member member) {
        // 동일한 username이 이미 존재하면 409 Conflict 반환
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // DB에 저장
        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/simple")
    public MemberDto registerSimpleMember(@RequestBody SimpleMemberRegisterDto dto) {
        Member saved = memberRepository.save(dto.toEntity());
        return MemberDto.from(saved);
    }

    // ✅ 로그인 (POST /api/members/login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 1. 유저 존재 여부 확인
        Optional<Member> memberOpt = memberRepository.findByUsername(username);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();

            // 2. 비밀번호 일치 여부 확인
            if (member.getPassword().equals(password)) {
                // 로그인 성공 시 일부 정보만 응답으로 전달
                Map<String, Object> result = new HashMap<>();
                result.put("id", member.getId());
                result.put("username", member.getUsername());
                result.put("name", member.getName());
                result.put("role", member.getRole());

                return ResponseEntity.ok(result);
            } else {
                // 비밀번호 불일치
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "비밀번호가 일치하지 않습니다"));
            }
        } else {
            // 해당 아이디 없음
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "해당 사용자가 존재하지 않습니다"));
        }
    }

    // ✅ 회원 단건 조회 (GET /api/members/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(member -> ResponseEntity.ok(MemberDto.from(member)))
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ 회원 정보 수정 (PUT /api/members/{id})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Member updated) {
        return memberRepository.findById(id).map(member -> {
            // 일반 정보 업데이트
            member.setName(updated.getName());
            member.setPhone(updated.getPhone());
            member.setRegion(updated.getRegion());
            member.setAge(updated.getAge());
            member.setRole(updated.getRole());

            // ✅ 신입 여부, 입장일, 마감일도 업데이트
            member.setIsNewcomer(updated.getIsNewcomer());
            member.setJoinedDate(updated.getJoinedDate());

            // 마감일은 입장일 + 1개월로 자동 설정
            if (updated.getJoinedDate() != null) {
                member.setAttendanceDeadline(updated.getJoinedDate().plusMonths(1));
            }

            memberRepository.save(member);
            return ResponseEntity.ok(member);
        }).orElse(ResponseEntity.notFound().build());
    }

}
