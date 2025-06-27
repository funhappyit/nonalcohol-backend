package com.nonalcohol.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor        // 기본 생성자
@AllArgsConstructor       // 모든 필드 받는 생성자
public class AttendanceStatDto {
    private Long id;
    private String name;
    private String username;
    private Long count;
}
