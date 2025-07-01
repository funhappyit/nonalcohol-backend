package com.nonalcohol.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;         // 이름
    private String nickname;     // 닉네임 (선택)

    private String phone;        // 연락처
    private String region;       // 지역 (ex. 군자, 건대)
    private String age;             // 나이

    private String status;       // 활동 상태: "참여", "비참여"
    private String role; // 예: ROLE_MEMBER, ROLE_ADMIN

    private String username; // 로그인 ID

    @JsonIgnore
    private String password; // 로그인 PW

    @Column(name = "is_newcomer")
    private Boolean isNewcomer;

    private LocalDate joinedDate;

    private LocalDate attendanceDeadline;

}