package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.MemberDto;
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
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 회원 등록
    @PostMapping
    public ResponseEntity<Member> register(@RequestBody Member member) {
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 이미 존재
        }
       // member.setRole("ROLE_MEMBER");
        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Member> memberOpt = memberRepository.findByUsername(username);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            if (member.getPassword().equals(password)) {
                // 사용자 정보 중 민감 정보 제외하고 반환
                Map<String, Object> result = new HashMap<>();
                result.put("id", member.getId());
                result.put("username", member.getUsername());
                result.put("name", member.getName());
                result.put("role", member.getRole());

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "비밀번호가 일치하지 않습니다"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "해당 사용자가 존재하지 않습니다"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(m -> {
                    MemberDto dto = new MemberDto(
                            m.getId(), m.getName(), m.getNickname(),
                            m.getPhone(), m.getRegion(), m.getAge(),
                            m.getStatus(), m.getRole(), m.getUsername());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Member updated) {
        return memberRepository.findById(id).map(member -> {
            member.setName(updated.getName());
            member.setPhone(updated.getPhone());
            member.setRegion(updated.getRegion());
            member.setAge(updated.getAge());
            member.setRole(updated.getRole());
            memberRepository.save(member);
            return ResponseEntity.ok(member);
        }).orElse(ResponseEntity.notFound().build());
    }

}