package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.entity.Member;
import com.nonalcohol.backend.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 🔧 AdminController.java
// 관리자용 회원 조회 및 삭제 기능을 제공하는 REST 컨트롤러

@RestController // JSON 형태의 HTTP 응답을 반환하는 컨트롤러
@RequestMapping("/api/admin") // 이 컨트롤러의 모든 경로는 "/api/admin"으로 시작됨
public class AdminController {

    private final MemberRepository memberRepository;

    // 💡 생성자 주입 방식으로 Repository 사용
    public AdminController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ✅ 전체 회원 목록 조회 API (GET /api/admin/members)
    @GetMapping("/members")
    public List<Member> getAllMembers() {
        // DB에 저장된 모든 회원을 조회하여 반환
        return memberRepository.findAll();
    }

    // ✅ 회원 삭제 API (DELETE /api/admin/members/{id})
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        // 해당 ID의 회원을 삭제
        memberRepository.deleteById(id);
        // 200 OK 응답만 반환 (내용 없음)
        return ResponseEntity.ok().build();
    }
}
