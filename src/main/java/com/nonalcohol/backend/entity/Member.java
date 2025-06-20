package com.nonalcohol.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
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
    private int age;             // 나이

    private String status;       // 활동 상태: "참여", "비참여"
    private String role;         // 역할: "운영자", "일반멤버"

    private String username; // 로그인 ID
    private String password; // 로그인 PW

}