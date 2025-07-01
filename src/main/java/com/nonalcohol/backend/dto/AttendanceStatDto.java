package com.nonalcohol.backend.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AttendanceStatDto {
    private Long id;
    private String name;
    private String username;
    private Long count;
    private String age;

    @QueryProjection // ✅ 여기에 붙이세요
    public AttendanceStatDto(Long id, String name, String username, Long count, String age) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.count = count;
        this.age = age;
    }
}
