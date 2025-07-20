package com.nonalcohol.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyParticipationDto {
    private String week;
    private long count;

}
