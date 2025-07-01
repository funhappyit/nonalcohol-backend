package com.nonalcohol.backend.dto;

import com.nonalcohol.backend.entity.Member;

import java.time.LocalDate;

public record SimpleMemberRegisterDto(
        String name,
        String age,
        String region,
        Boolean isNewcomer, // 프론트에서 받아옴
        LocalDate joinedDate // 프론트에서 받은 날짜
) {
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .region(region)
                .isNewcomer(isNewcomer != null ? isNewcomer : true)
                .joinedDate(joinedDate)
                .attendanceDeadline(
                        joinedDate != null ? joinedDate.plusMonths(1) : null
                )
                .role("ROLE_MEMBER")
                .build();
    }
}
