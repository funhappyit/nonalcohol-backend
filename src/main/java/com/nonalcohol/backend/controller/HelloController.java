package com.nonalcohol.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {


    @GetMapping("/admin/hello")
    public String admin() {
        return "관리자 전용!";
    }

    @GetMapping("/member/hello")
    public String member() {
        return "멤버 또는 관리자 접근 가능!";
    }
}
