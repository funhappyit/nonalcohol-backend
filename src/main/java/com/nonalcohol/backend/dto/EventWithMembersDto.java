package com.nonalcohol.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventWithMembersDto {
    private String title;
    private String location;
    private String date;
    private List<Long> memberIds;
    private List<String> usernames;
}
