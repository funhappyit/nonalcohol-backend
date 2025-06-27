package com.nonalcohol.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventWithMembersDto {
    private String title;
    private String location;
    private String date;
    private Long id; // ✅ 수정용 ID 필드 추가
    private List<Long> memberIds;// ✅ 참여자 ID 목록
    private List<String> memberNames; // ✅ 이름 리스트로 설정
}
