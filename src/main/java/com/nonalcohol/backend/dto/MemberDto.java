package com.nonalcohol.backend.dto;

public record MemberDto(
        Long id,
        String name,
        String nickname,
        String phone,
        String region,
        int age,
        String status,
        String role,
        String username
) {


}
