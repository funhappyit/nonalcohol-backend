package com.nonalcohol.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor        // 기본 생성자
@AllArgsConstructor
public class MemberRankingDto {

    private String name;
    private int count;
}
