package com.nonalcohol.backend.controller;

import com.nonalcohol.backend.dto.LabelCountDto;
import com.nonalcohol.backend.dto.MemberRankingDto;
import com.nonalcohol.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;



    @GetMapping("/region")
    public List<LabelCountDto> getRegionChartData() {
        return dashboardService.getRegionDistribution();
    }

    @GetMapping("/monthly-ranking")
    public List<MemberRankingDto> getMonthlyRanking() {
        return dashboardService.getMonthlyRanking();
    }


}
