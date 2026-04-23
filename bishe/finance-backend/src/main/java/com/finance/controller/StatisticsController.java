package com.finance.controller;

import com.finance.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/monthly/{userId}/{year}/{month}")
    public ResponseEntity<?> getMonthlyStatistics(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        try {
            Map<String, Object> statistics = statisticsService.getMonthlyStatistics(userId, year, month);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取月度统计失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/yearly/{userId}/{year}")
    public ResponseEntity<?> getYearlyStatistics(@PathVariable Long userId, @PathVariable int year) {
        try {
            Map<String, Object> statistics = statisticsService.getYearlyStatistics(userId, year);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取年度统计失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}