package com.nonalcohol.backend.dto;

import com.nonalcohol.backend.entity.Member;

import java.time.LocalDate;

public record MemberDto(
        Long id,
        String name,
        String nickname,
        String phone,
        String region,
        int age,
        String status,
        String role,
        String username,
        Boolean isNewcomer,
        LocalDate joinedDate,
        LocalDate attendanceDeadline
) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getPhone(),
                member.getRegion(),
                member.getAge(),
                member.getStatus(),
                member.getRole(),
                member.getUsername(),
                member.getIsNewcomer(),
                member.getJoinedDate(),
                member.getAttendanceDeadline()
        );
    }
}
